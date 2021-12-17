package com.chis.communityhealthis.service.communityuser;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.factory.AuditBeanFactory;
import com.chis.communityhealthis.model.address.AddressModel;
import com.chis.communityhealthis.model.filter.CommunityUserBeanQuery;
import com.chis.communityhealthis.model.health.HealthModel;
import com.chis.communityhealthis.model.occupation.OccupationModel;
import com.chis.communityhealthis.model.signup.*;
import com.chis.communityhealthis.model.user.CommunityUserModel;
import com.chis.communityhealthis.model.user.CommunityUserProfileModel;
import com.chis.communityhealthis.model.user.PersonalDetailModel;
import com.chis.communityhealthis.repository.account.AccountDao;
import com.chis.communityhealthis.repository.address.AddressDao;
import com.chis.communityhealthis.repository.communityuser.CommunityUserDao;
import com.chis.communityhealthis.repository.disease.DiseaseDao;
import com.chis.communityhealthis.repository.healthissue.HealthIssueDao;
import com.chis.communityhealthis.repository.occupation.OccupationDao;
import com.chis.communityhealthis.service.audit.AuditService;
import com.chis.communityhealthis.service.sms.SmsService;
import com.chis.communityhealthis.utility.*;
import com.google.maps.model.LatLng;
import javassist.NotFoundException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommunityUserServiceImpl implements CommunityUserService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CommunityUserDao communityUserDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private OccupationDao occupationDao;

    @Autowired
    private HealthIssueDao healthIssueDao;

    @Autowired
    private DiseaseDao diseaseDao;

    @Autowired
    private SmsService smsService;

    @Autowired
    private AuditService auditService;

    @Override
    public List<CommunityUserModel> getCommunityUsers(CommunityUserBeanQuery filter) {
        List<CommunityUserModel> list = new ArrayList<>();
        List<CommunityUserBean> communityUserBeans = communityUserDao.getCommunityUsers(filter);
        if (!CollectionUtils.isEmpty(communityUserBeans)) {
            List<String> usernameList = communityUserBeans.stream()
                    .map(CommunityUserBean::getUsername)
                    .collect(Collectors.toList());
            Map<String, AccountBean> accountBeanMap = new HashMap<>();
            List<AccountBean> accountBeans = accountDao.findAccounts(usernameList);
            if (!CollectionUtils.isEmpty(accountBeans)) {
                for (AccountBean accountBean : accountBeans) {
                    accountBeanMap.put(accountBean.getUsername(), accountBean);
                }
            }

            for (CommunityUserBean userBean : communityUserBeans) {
                AccountBean accountBean = accountBeanMap.getOrDefault(userBean.getUsername(), null);
                list.add(toCommunityUserModel(userBean, accountBean));
            }
        }
        Collections.sort(list);
        return list;
    }

    private CommunityUserModel toCommunityUserModel(CommunityUserBean userBean, AccountBean accountBean) {
        CommunityUserModel model = new CommunityUserModel();
        model.setAccIsActivate(accountBean.getIsActive());
        model.setEmail(accountBean.getEmail());
        model.setUsername(userBean.getUsername());
        model.setFullName(userBean.getFullName());
        model.setEthnic(userBean.getEthnic());
        model.setGender(userBean.getGender());
        model.setNric(userBean.getNric());
        model.setContactNo(userBean.getContactNo());

        if (userBean.getAddressBean() != null) {
            model.setAddress(toAddressModel(userBean.getAddressBean()));
        }

        if (!CollectionUtils.isEmpty(userBean.getHealthIssueBeans())) {
            List<HealthModel> healthModelList = new ArrayList<>();
            for (HealthIssueBean bean : userBean.getHealthIssueBeans()) {
                healthModelList.add(toHealthModel(bean));
            }
            model.setHealthIssues(healthModelList);
        }
        return model;
    }

    @Override
    public CommunityUserProfileModel getCommunityUserProfile(String username) throws NotFoundException {
        AccountBean accountBean = accountDao.findAccount(username);
        if (accountBean == null) {
            throw new NotFoundException("Account [username: " + username + "] was not found.");
        }
        CommunityUserBean communityUserBean = communityUserDao.getCommunityUser(username);
        return toCommunityUserProfileModel(accountBean, communityUserBean);
    }

    @Override
    public void approveUserAccount(String username, String actionMakerUsername) throws Exception {
        AccountBean accountBean = accountDao.find(username);
        if (accountBean == null) {
            throw new Exception("Account [username: " + username + "] was not found.");
        }
        accountBean.setIsActive(FlagConstant.YES);
        accountDao.update(accountBean);

        OccupationBean occupationBean = occupationDao.find(username);
        if (occupationBean != null) {
            occupationBean.setApprovedBy(actionMakerUsername);
            occupationBean.setApprovedDate(new Date());
            occupationDao.update(occupationBean);
        }

        List<HealthIssueBean> healthIssueBeans = healthIssueDao.findHealthIssueBeans(username);
        if (!CollectionUtils.isEmpty(healthIssueBeans)) {
            for (HealthIssueBean bean : healthIssueBeans) {
                bean.setApprovedBy(actionMakerUsername);
                bean.setApprovedDate(new Date());
                healthIssueDao.update(bean);
            }
        }

        CommunityUserBean communityUserBean = communityUserDao.getCommunityUser(username);
        smsService.sendSms(communityUserBean.getContactNo(), "Your account has been approved. You may login now.");

        AuditBeanFactory auditBeanFactory = new AuditBeanFactory(AuditConstant.MODULE_COMMUNITY_USER, AuditConstant.formatActionApproveCommunityUser(username, communityUserBean.getFullName()), actionMakerUsername);
        AuditBean auditBean = auditBeanFactory.createAuditBean();
        auditService.saveLogs(auditBean, null);
    }

    @Override
    public void rejectUserAccount(String username, String actionMakerUsername) throws Exception {
        try {
            CommunityUserBean communityUserBean = removeUser(username);
            smsService.sendSms(communityUserBean.getContactNo(), "Your account has been rejected. Please contact admin for more info.");
            AuditBeanFactory auditBeanFactory = new AuditBeanFactory(AuditConstant.MODULE_COMMUNITY_USER, AuditConstant.formatActionRejectCommunityUser(username, communityUserBean.getFullName()), actionMakerUsername);
            AuditBean auditBean = auditBeanFactory.createAuditBean();
            auditService.saveLogs(auditBean, null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void deleteUserAccount(String username, String actionMakerUsername) throws Exception {
        try {
            CommunityUserBean communityUserBean = removeUser(username);
            AuditBeanFactory auditBeanFactory = new AuditBeanFactory(AuditConstant.MODULE_COMMUNITY_USER, AuditConstant.formatActionDeleteCommunityUser(username, communityUserBean.getFullName()), actionMakerUsername);
            AuditBean auditBean = auditBeanFactory.createAuditBean();
            auditService.saveLogs(auditBean, null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private CommunityUserBean removeUser(String username) {
        OccupationBean occupationBean = occupationDao.find(username);
        if (occupationBean != null) {
            occupationDao.remove(occupationBean);
        }

        AddressBean addressBean = addressDao.find(username);
        if (addressBean != null) {
            addressDao.remove(addressBean);
        }

        List<HealthIssueBean> healthIssueBeans = healthIssueDao.findHealthIssueBeans(username);
        if (!CollectionUtils.isEmpty(healthIssueBeans)) {
            for (HealthIssueBean bean : healthIssueBeans) {
                healthIssueDao.remove(bean);
            }
        }

        CommunityUserBean communityUserBean = communityUserDao.find(username);
        CommunityUserBean clonedBean = SerializationUtils.clone(communityUserBean);

        if (communityUserBean != null) {
            communityUserDao.remove(communityUserBean);
        }

        AccountBean accountBean = accountDao.find(username);
        if (accountBean != null) {
            accountDao.remove(accountBean);
        }

        return clonedBean;
    }

    @Override
    public void updateUserAccount(AccountRegistrationForm form, String actionMaker, boolean isAdmin) throws Exception {
        String username = form.getPersonalDetail().getUsername();

        AccountBean accountBean = accountDao.findAccount(username);
        if (accountBean == null) {
            throw new NotFoundException("Account [username: " + username + "] was not found.");
        }
        AccountBean clonedAccountBean = SerializationUtils.clone(accountBean);

        accountBean.setEmail(form.getPersonalDetail().getEmail());
        accountDao.update(accountBean);
        BeanComparator accountBeanComparator = new BeanComparator(clonedAccountBean, accountBean);

        CommunityUserBean communityUserBean = communityUserDao.getCommunityUser(username);
        BeanComparator communityUserBeanComparator = updateCommunityUserBean(communityUserBean, form.getPersonalDetail());

        AddressBean addressBean = addressDao.find(username);
        BeanComparator addressBeanComparator = updateAddressBean(addressBean, form.getAddress());

        BeanComparator occupationBeanComparator = null;
        if (form.getOccupation() != null) {
            OccupationBean occupationBean = occupationDao.find(username);
            if (occupationBean == null) {
                occupationBean = new OccupationBean();
                occupationBean.setUsername(username);
            }
            occupationBeanComparator = updateOccupationBean(occupationBean, form.getOccupation());
        }

        List<AuditActionBean> healthIssueBeanAuditActionBeans = new ArrayList<>();
        if (form.getHealth() != null) {
            List<HealthIssueBean> healthIssueBeans = healthIssueDao.findHealthIssueBeans(username);
            healthIssueBeanAuditActionBeans = updateHealthIssueBeans(healthIssueBeans, form.getHealth(), username, actionMaker, isAdmin);
        }

        AuditBeanFactory auditBeanFactory = new AuditBeanFactory(AuditConstant.MODULE_COMMUNITY_USER, AuditConstant.formatActionUpdateCommunityUser(username, communityUserBean.getFullName()), actionMaker);
        AuditBean auditBean = auditBeanFactory.createAuditBean();
        List<AuditActionBean> auditActionBeans = getAuditActionBeans(accountBeanComparator, communityUserBeanComparator, addressBeanComparator, occupationBeanComparator);
        if (!CollectionUtils.isEmpty(healthIssueBeanAuditActionBeans)) {
            auditActionBeans.addAll(healthIssueBeanAuditActionBeans);
        }
        auditService.saveLogs(auditBean, auditActionBeans);
    }

    private List<AuditActionBean> getAuditActionBeans(BeanComparator accountBeanComparator,
                                                      BeanComparator communityUserBeanComparator,
                                                      BeanComparator addressBeanComparator,
                                                      BeanComparator occupationBeanComparator) {
        List<AuditActionBean> list = new ArrayList<>();
        if (accountBeanComparator.hasChanges()) {
            list.add(new AuditActionBean(accountBeanComparator.toPrettyString()));
        }
        if (communityUserBeanComparator.hasChanges()) {
            list.add(new AuditActionBean(communityUserBeanComparator.toPrettyString()));
        }
        if (addressBeanComparator.hasChanges()) {
            list.add(new AuditActionBean(addressBeanComparator.toPrettyString()));
        }
        if (occupationBeanComparator != null && occupationBeanComparator.hasChanges()) {
            list.add(new AuditActionBean(occupationBeanComparator.toPrettyString()));
        }
        return list;
    }

    private List<AuditActionBean> updateHealthIssueBeans(List<HealthIssueBean> healthIssueBeans, HealthForm form, String username, String actionMaker, boolean isAdmin) {
        Map<Integer, String> diseaseMap = initDiseaseMap();
        Map<Integer, HealthIssueBean> healthIssueBeanMap = initHealthIssueBeanMap(healthIssueBeans);
        List<Integer> oriIds = new ArrayList<>(healthIssueBeanMap.keySet());
        List<Integer> newIds = new ArrayList<>();

        List<AuditActionBean> auditActionBeans = new ArrayList<>();

        if (!CollectionUtils.isEmpty(form.getDiseaseList())) {
            for (HealthIssueModel model : form.getDiseaseList()) {
                HealthIssueBean bean;
                if (model.getHealthIssueId() != null) {
                    newIds.add(model.getHealthIssueId());
                    bean = healthIssueBeanMap.get(model.getHealthIssueId());
                    HealthIssueBean clonedBean = SerializationUtils.clone(bean);
                    boolean hasChanges = false;
                    boolean approvalHadChanges = isApproved(bean) != model.getIsApproved();
                    if (!StringUtils.equals(model.getDescription(), bean.getIssueDescription()) || !bean.getDiseaseId().equals(model.getDiseaseId())) {
                        hasChanges = true;
                        bean.setDiseaseId(model.getDiseaseId());
                        bean.setIssueDescription(model.getDescription());
                    }
                    if (hasChanges || approvalHadChanges) {
                        updateHealthIssueBeanApproval(bean, model, hasChanges, isAdmin, actionMaker);
                        healthIssueDao.update(bean);
                        BeanComparator healthIssueBeanComparator = new BeanComparator(clonedBean, bean);
                        if (healthIssueBeanComparator.hasChanges()) {
                            auditActionBeans.add(new AuditActionBean(healthIssueBeanComparator.toPrettyString()));
                        }
                    }
                } else {
                    bean = toHealthIssueBean(model);
                    bean.setUsername(username);
                    bean.setCreatedBy(actionMaker);
                    bean.setCreatedDate(new Date());
                    if (isAdmin) {
                        bean.setApprovedBy(actionMaker);
                        bean.setApprovedDate(new Date());
                    }
                    healthIssueDao.add(bean);
                    String diseaseName = diseaseMap.getOrDefault(bean.getDiseaseId(), "-");
                    String msg = "Added health issue bean [diseaseName: " + diseaseName + "]";
                    if (StringUtils.isNotBlank(bean.getIssueDescription())) {
                        msg += " [diseaseDescription: " + bean.getIssueDescription() + "]";
                    }
                    auditActionBeans.add(new AuditActionBean(msg));
                }
            }
        }

        ListComparator<Integer> listComparator = new ListComparator<>(oriIds, newIds);
        List<Integer> idsToBeDeleted = listComparator.getListToDelete();
        if (!CollectionUtils.isEmpty(idsToBeDeleted)) {
            List<HealthIssueBean> listToRemove = healthIssueBeans.stream().filter(bean -> idsToBeDeleted.contains(bean.getIssueId())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(listToRemove)) {
                for (HealthIssueBean bean : listToRemove) {
                    HealthIssueBean clonedBean = SerializationUtils.clone(bean);
                    healthIssueDao.remove(bean);
                    String diseaseName = diseaseMap.getOrDefault(clonedBean.getDiseaseId(), "-");
                    String msg = "Deleted health issue bean [diseaseName: " + diseaseName + "]";
                    if (StringUtils.isNotBlank(clonedBean.getIssueDescription())) {
                        msg += " [diseaseDescription: " + bean.getIssueDescription() + "]";
                    }
                    auditActionBeans.add(new AuditActionBean(msg));
                }
            }
        }
        return auditActionBeans;
    }

    private Map<Integer, String> initDiseaseMap() {
        Map<Integer, String> map = new HashMap<>();
        List<DiseaseBean> diseaseBeans = diseaseDao.getAll();
        if (!CollectionUtils.isEmpty(diseaseBeans)) {
            for (DiseaseBean diseaseBean : diseaseBeans) {
                if (!map.containsKey(diseaseBean.getDiseaseId())) {
                    map.put(diseaseBean.getDiseaseId(), diseaseBean.getDiseaseName());
                }
            }
        }
        return map;
    }

    private void updateHealthIssueBeanApproval(HealthIssueBean bean, HealthIssueModel model, boolean hasChanges, boolean isAdmin, String actionMaker) {
        if (hasChanges) {
            if (isAdmin) {
                bean.setApprovedBy(actionMaker);
                bean.setApprovedDate(new Date());
            } else {
                bean.setApprovedBy(null);
                bean.setApprovedDate(null);
            }
        } else {
            if (model.getIsApproved() && !isApproved(bean) && isAdmin) {
                bean.setApprovedBy(actionMaker);
                bean.setApprovedDate(new Date());
            } else if (!isAdmin || !model.getIsApproved() && isApproved(bean)) {
                bean.setApprovedBy(null);
                bean.setApprovedDate(null);
            }
        }
    }

    private Map<Integer, HealthIssueBean> initHealthIssueBeanMap(List<HealthIssueBean> healthIssueBeans) {
        Map<Integer, HealthIssueBean> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(healthIssueBeans)) {
            for (HealthIssueBean bean : healthIssueBeans) {
                map.put(bean.getIssueId(), bean);
            }
        }
        return map;
    }

    private HealthIssueBean toHealthIssueBean(HealthIssueModel model) {
        HealthIssueBean bean = new HealthIssueBean();
        bean.setDiseaseId(model.getDiseaseId());
        bean.setIssueDescription(model.getDescription());
        bean.setCreatedDate(new Date());
        return bean;
    }

    private BeanComparator updateOccupationBean(OccupationBean occupationBean, OccupationForm form) {
        OccupationBean cloneOccupationBean = SerializationUtils.clone(occupationBean);

        occupationBean.setEmploymentType(form.getEmploymentType());
        if (StringUtils.equals("-", form.getEmploymentType())) {
            occupationBean.setSalary(0.00);
            occupationBean.setOccupationName(null);
            occupationBean.setEmployerCompany(null);
            occupationBean.setEmployerContactNo(null);
        } else {
            occupationBean.setSalary(form.getSalary());
            occupationBean.setOccupationName(form.getOccupationName());

            if (!StringUtils.equals("S/E", form.getEmploymentType())) {
                occupationBean.setEmployerCompany(form.getEmployerCompany());
                occupationBean.setEmployerContactNo(form.getEmployerContactNo());
            }
        }

        occupationDao.update(occupationBean);
        return new BeanComparator(cloneOccupationBean, occupationBean);
    }

    private BeanComparator updateAddressBean(AddressBean addressBean, AddressForm address) {
        AddressBean cloneAddressBean = SerializationUtils.clone(addressBean);
        addressBean.setAddressLine1(address.getAddressLine1());
        addressBean.setAddressLine2(address.getAddressLine2());
        addressBean.setPostcode(address.getPostcode());
        addressBean.setCity(address.getCity());
        addressBean.setState(address.getState());
        addressDao.update(addressBean);
        return new BeanComparator(cloneAddressBean, addressBean);
    }

    private BeanComparator updateCommunityUserBean(CommunityUserBean communityUserBean, PersonalDetailForm personalDetail) {
        CommunityUserBean clonedCommunityUserBean = SerializationUtils.clone(communityUserBean);
        communityUserBean.setContactNo(personalDetail.getContactNo());
        communityUserBean.setNric(personalDetail.getNric());
        communityUserBean.setGender(personalDetail.getGender());
        communityUserBean.setEthnic(personalDetail.getEthnic());
        communityUserDao.update(communityUserBean);
        return new BeanComparator(clonedCommunityUserBean, communityUserBean);
    }

    private CommunityUserProfileModel toCommunityUserProfileModel(AccountBean accountBean, CommunityUserBean communityUserBean) {
        CommunityUserProfileModel model = new CommunityUserProfileModel();

        AddressBean addressBean = null;
        OccupationBean occupationBean = null;
        Set<HealthIssueBean> healthIssueBeans = null;
        if (communityUserBean != null) {
            addressBean = communityUserBean.getAddressBean();
            occupationBean = communityUserBean.getOccupationBean();
            healthIssueBeans = communityUserBean.getHealthIssueBeans();
        }

        if (accountBean != null) {
            model.setUsername(accountBean.getUsername());
            model.setAccIsActivate(accountBean.getIsActive());
            model.setEmail(accountBean.getEmail());
        }

        if (communityUserBean != null) {
            model.setPersonalDetail(toPersonalDetailModel(communityUserBean));
        }

        if (addressBean != null) {
            model.setAddress(toAddressModel(addressBean));
        }

        if (occupationBean != null) {
            model.setOccupation(toOccupationModel(occupationBean));
        }

        if (!CollectionUtils.isEmpty(healthIssueBeans)) {
            List<HealthModel> healthModelList = new ArrayList<>();
            for (HealthIssueBean bean : communityUserBean.getHealthIssueBeans()) {
                healthModelList.add(toHealthModel(bean));
            }
            model.setHealthModelList(healthModelList);
        }

        return model;
    }

    private PersonalDetailModel toPersonalDetailModel(CommunityUserBean communityUserBean) {
        PersonalDetailModel model = new PersonalDetailModel();
        model.setFullName(communityUserBean.getFullName());
        model.setNric(communityUserBean.getNric());
        model.setGender(communityUserBean.getGender());
        model.setContactNo(communityUserBean.getContactNo());
        model.setEthnic(communityUserBean.getEthnic());
        return model;
    }

    private AddressModel toAddressModel(AddressBean addressBean) {
        AddressModel model = new AddressModel();
        model.setLine1(addressBean.getAddressLine1());
        model.setLine2(addressBean.getAddressLine2());
        model.setPostcode(addressBean.getPostcode());
        model.setCity(addressBean.getCity());
        model.setState(addressBean.getState());
        model.setFullAddress(AddressUtil.generateFullAddress(addressBean));
        if (addressBean.getLatitude() != null && addressBean.getLongitude() != null) {
            LatLng latLng = new LatLng(addressBean.getLatitude(), addressBean.getLongitude());
            model.setLatLng(latLng);
        }
        if (addressBean.getZoneBean() != null) {
            model.setZoneName(addressBean.getZoneBean().getZoneName());
        }
        return model;
    }

    private OccupationModel toOccupationModel(OccupationBean occupationBean) {
        OccupationModel model = new OccupationModel();
        model.setEmploymentStatus(occupationBean.getEmploymentType());
        model.setOccupationName(occupationBean.getOccupationName());
        model.setSalary(occupationBean.getSalary());
        model.setEmployerCompany(occupationBean.getEmployerCompany());
        model.setEmployerContactNo(occupationBean.getEmployerContactNo());
        return model;
    }

    private HealthModel toHealthModel(HealthIssueBean bean) {
        HealthModel model = new HealthModel();
        model.setIssueId(bean.getIssueId());
        model.setDiseaseId(bean.getDiseaseId());
        model.setDiseaseName(bean.getDiseaseBean().getDiseaseName());
        model.setDiseaseDescription(bean.getIssueDescription());
        model.setUsername(bean.getUsername());
        if (bean.getAdminBean() != null) {
            model.setApprovedBy(bean.getAdminBean().getFullName());
            model.setApprovedByUsername(bean.getApprovedBy());
            model.setApprovedDate(bean.getApprovedDate());
        }
        return model;
    }

    private boolean isApproved(HealthIssueBean bean) {
        return bean.getApprovedDate() != null && StringUtils.isNotBlank(bean.getApprovedBy());
    }
}
