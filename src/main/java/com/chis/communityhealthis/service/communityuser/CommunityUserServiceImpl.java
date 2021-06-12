package com.chis.communityhealthis.service.communityuser;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.model.health.HealthModel;
import com.chis.communityhealthis.model.signup.*;
import com.chis.communityhealthis.model.user.CommunityUserProfileModel;
import com.chis.communityhealthis.model.user.CommunityUserTableModel;
import com.chis.communityhealthis.repository.AccountDao;
import com.chis.communityhealthis.repository.address.AddressDao;
import com.chis.communityhealthis.repository.communityuser.CommunityUserDao;
import com.chis.communityhealthis.repository.healthIssue.HealthIssueDao;
import com.chis.communityhealthis.repository.occupation.OccupationDao;
import com.chis.communityhealthis.utility.FlagConstant;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommunityUserServiceImpl implements CommunityUserService{

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
    public List<CommunityUserTableModel> getCommunityUsers() {
        List<CommunityUserTableModel> list = new ArrayList<>();

        List<CommunityUserBean> communityUserBeans = communityUserDao.getAll();
        if (CollectionUtils.isEmpty(communityUserBeans)) {
            return list;
        }

        List<String> usernames = communityUserBeans.stream()
                .map(CommunityUserBean::getUsername)
                .collect(Collectors.toList());

        List<AccountBean> accountBeans = accountDao.findAccounts(usernames);
        Map<String, String> accountStatusMap = accountBeans.stream()
                .collect(Collectors.toMap(AccountBean::getUsername, AccountBean::getIsActive, (x,y)-> x + ", " + y, LinkedHashMap::new));

        for (CommunityUserBean communityUserBean : communityUserBeans) {
            CommunityUserTableModel model = new CommunityUserTableModel();
            model.setUsername(communityUserBean.getUsername());
            model.setFullName(communityUserBean.getFullName());
            model.setNricNo(communityUserBean.getNric());
            model.setEmail(communityUserBean.getEmail());
            model.setIsActive(accountStatusMap.get(communityUserBean.getUsername()));
            list.add(model);
        }

        Collections.sort(list);
        return list;
    }

    @Override
    public CommunityUserProfileModel getCommunityUserProfile(String username) {
        AccountBean accountBean = accountDao.findAccount(username);
        Assert.notNull(accountBean, "Account [username: " + username + "] was not found.");
        CommunityUserBean communityUserBean = communityUserDao.find(username);
        AddressBean addressBean = addressDao.find(username);
        OccupationBean occupationBean = occupationDao.find(username);
        List<HealthIssueBean> healthIssueBeans = healthIssueDao.findHealthIssueBeans(username);

        CommunityUserProfileModel profileModel = toCommunityUserProfileModel(communityUserBean, addressBean, occupationBean, healthIssueBeans);
        profileModel.setAccIsActivate(accountBean.getIsActive());
        return profileModel;
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
            for (HealthIssueBean bean: healthIssueBeans) {
                bean.setApprovedBy(adminUsername);
                bean.setApprovedDate(new Date());
                healthIssueDao.saveOrUpdate(bean);
            }
        }

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
    public void updateUserAccount(AccountRegistrationForm form) {
        String username = form.getPersonalDetail().getUsername();

        CommunityUserBean communityUserBean = communityUserDao.find(username);
        updateCommunityUserBean(communityUserBean, form.getPersonalDetail());

        AddressBean addressBean = addressDao.find(username);
        updateAddressBean(addressBean, form.getAddress());

        OccupationBean occupationBean = occupationDao.find(username);
        updateOccupationBean(occupationBean, form.getOccupation());

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
        addressDao.saveOrUpdate(addressBean);
    }

    private void updateCommunityUserBean(CommunityUserBean communityUserBean, PersonalDetailForm personalDetail) {
        communityUserBean.setEmail(personalDetail.getEmail());
        communityUserBean.setContactNo(personalDetail.getContactNo());
        communityUserBean.setNric(personalDetail.getNric());
        communityUserBean.setGender(personalDetail.getGender());
        communityUserBean.setEthnic(personalDetail.getEthnic());
        communityUserDao.saveOrUpdate(communityUserBean);
    }

    private CommunityUserProfileModel toCommunityUserProfileModel(CommunityUserBean communityUserBean, AddressBean addressBean, OccupationBean occupationBean, List<HealthIssueBean> healthIssueBeans) {
        CommunityUserProfileModel model = new CommunityUserProfileModel();
        model.setPersonalDetail(communityUserBean);
        model.setAddress(addressBean);
        model.setOccupation(occupationBean);
        if (!CollectionUtils.isEmpty(healthIssueBeans)) {
            List<HealthModel> list = new ArrayList<>();
            for (HealthIssueBean bean : healthIssueBeans) {
                list.add(toHealthModel(bean));
            }
            model.setHealthModelList(list);
        }
        return model;
    }

    private HealthModel toHealthModel(HealthIssueBean bean) {
        HealthModel model = new HealthModel();
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
