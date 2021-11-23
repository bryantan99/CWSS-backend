package com.chis.communityhealthis.service.communityuser;

import com.chis.communityhealthis.bean.*;
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
import com.chis.communityhealthis.repository.healthissue.HealthIssueDao;
import com.chis.communityhealthis.repository.occupation.OccupationDao;
import com.chis.communityhealthis.utility.AddressUtil;
import com.chis.communityhealthis.utility.FlagConstant;
import com.google.maps.model.LatLng;
import io.jsonwebtoken.lang.Assert;
import javassist.NotFoundException;
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
    public Boolean approveUserAccount(String username, String adminUsername) {
        AccountBean accountBean = accountDao.find(username);
        Assert.notNull(accountBean, "Account [username: " + username + "] was not found.");
        accountBean.setIsActive(FlagConstant.YES);
        accountDao.saveOrUpdate(accountBean);

        OccupationBean occupationBean = occupationDao.find(username);
        Assert.notNull(occupationBean, "Occupation bean [username: " + username + "] was not found.");
        occupationBean.setApprovedBy(adminUsername);
        occupationBean.setApprovedDate(new Date());
        occupationDao.saveOrUpdate(occupationBean);

        List<HealthIssueBean> healthIssueBeans = healthIssueDao.findHealthIssueBeans(username);
        if (!CollectionUtils.isEmpty(healthIssueBeans)) {
            for (HealthIssueBean bean : healthIssueBeans) {
                bean.setApprovedBy(adminUsername);
                bean.setApprovedDate(new Date());
                healthIssueDao.saveOrUpdate(bean);
            }
        }

        return true;
    }

    @Override
    public Boolean rejectUserAccount(String username) {
        deleteUserAccount(username);
        return true;
    }

    @Override
    public void deleteUserAccount(String username) {
        OccupationBean occupationBean = occupationDao.find(username);
        occupationDao.remove(occupationBean);

        AddressBean addressBean = addressDao.find(username);
        addressDao.remove(addressBean);

        List<HealthIssueBean> healthIssueBeans = healthIssueDao.findHealthIssueBeans(username);
        if (!CollectionUtils.isEmpty(healthIssueBeans)) {
            for (HealthIssueBean bean : healthIssueBeans) {
                healthIssueDao.remove(bean);
            }
        }

        CommunityUserBean communityUserBean = communityUserDao.find(username);
        communityUserDao.remove(communityUserBean);

        AccountBean accountBean = accountDao.find(username);
        accountDao.remove(accountBean);
    }

    @Override
    public void updateUserAccount(AccountRegistrationForm form) throws Exception {
        String username = form.getPersonalDetail().getUsername();

        AccountBean accountBean = accountDao.findAccount(username);
        if (accountBean == null) {
            throw new NotFoundException("Account [username: " + username + "] was not found.");
        }

        accountBean.setEmail(form.getPersonalDetail().getEmail());
        accountDao.update(accountBean);

        CommunityUserBean communityUserBean = communityUserDao.getCommunityUser(username);
        updateCommunityUserBean(communityUserBean, form.getPersonalDetail());

        AddressBean addressBean = addressDao.find(username);
        updateAddressBean(addressBean, form.getAddress());

        if (form.getOccupation() != null) {
            OccupationBean occupationBean = occupationDao.find(username);
            if (occupationBean == null) {
                occupationBean = new OccupationBean();
                occupationBean.setUsername(username);
            }
            updateOccupationBean(occupationBean, form.getOccupation());
        }

        if (form.getHealth() != null && !CollectionUtils.isEmpty(form.getHealth().getDiseaseList())) {
            List<HealthIssueBean> healthIssueBeans = healthIssueDao.findHealthIssueBeans(username);
            updateHealthIssueBeans(healthIssueBeans, form.getHealth());
        }
    }

    private void updateHealthIssueBeans(List<HealthIssueBean> healthIssueBeans, HealthForm health) {
        //  ToDo: Update Health Issue Beans
    }

    private void updateOccupationBean(OccupationBean occupationBean, OccupationForm form) {
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

        occupationDao.saveOrUpdate(occupationBean);
    }

    private void updateAddressBean(AddressBean addressBean, AddressForm address) {
        addressBean.setAddressLine1(address.getAddressLine1());
        addressBean.setAddressLine2(address.getAddressLine2());
        addressBean.setPostcode(address.getPostcode());
        addressBean.setCity(address.getCity());
        addressBean.setState(address.getState());
        addressDao.update(addressBean);
    }

    private void updateCommunityUserBean(CommunityUserBean communityUserBean, PersonalDetailForm personalDetail) {
        communityUserBean.setContactNo(personalDetail.getContactNo());
        communityUserBean.setNric(personalDetail.getNric());
        communityUserBean.setGender(personalDetail.getGender());
        communityUserBean.setEthnic(personalDetail.getEthnic());
        communityUserDao.update(communityUserBean);
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
        model.setZoneName(addressBean.getZoneBean().getZoneName());

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
}
