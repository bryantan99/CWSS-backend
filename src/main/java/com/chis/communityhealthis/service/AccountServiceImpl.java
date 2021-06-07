package com.chis.communityhealthis.service;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.bean.AddressBean;
import com.chis.communityhealthis.bean.CommunityUserBean;
import com.chis.communityhealthis.bean.OccupationBean;
import com.chis.communityhealthis.model.account.AccountModel;
import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
import com.chis.communityhealthis.model.signup.AddressForm;
import com.chis.communityhealthis.model.signup.OccupationForm;
import com.chis.communityhealthis.model.signup.PersonalDetailForm;
import com.chis.communityhealthis.repository.AccountDao;
import com.chis.communityhealthis.repository.address.AddressDao;
import com.chis.communityhealthis.repository.communityuser.CommunityUserDao;
import com.chis.communityhealthis.repository.occupation.OccupationDao;
import com.chis.communityhealthis.utility.CommunityServiceCentreConstant;
import com.chis.communityhealthis.utility.FlagConstant;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CommunityUserDao communityUserDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private OccupationDao occupationDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AccountModel addAccount(AccountRegistrationForm form) {
        AccountBean accountBean = createAccountBean(form.getPersonalDetail());
        String username = accountDao.add(accountBean);

        CommunityUserBean communityUserBean = createCommunityUserBean(form.getPersonalDetail());
        communityUserDao.add(communityUserBean);

        AddressBean addressBean = createAddressBean(username, form.getAddress());
        addressDao.add(addressBean);

        OccupationBean occupationBean = createOccupationBean(username, form.getOccupation());
        occupationDao.add(occupationBean);

        return new AccountModel(accountBean.getUsername(), accountBean.getIsActive());
    }

    @Override
    public AccountBean getAccountWithRoles(String username) {
        return accountDao.findAccountWithRoles(username);
    }

    @Override
    public Boolean isValidUsername(String username) {
        AccountBean accountBean = accountDao.find(username);
        return accountBean == null;
    }

    private AccountBean createAccountBean(PersonalDetailForm personalDetail) {
        Assert.isTrue(StringUtils.equals(personalDetail.getPassword(), personalDetail.getConfirmPassword()), "Password does not matched");
        AccountBean bean = new AccountBean();
        bean.setUsername(personalDetail.getUsername());
        bean.setPw(bCryptPasswordEncoder.encode(personalDetail.getPassword()));
        bean.setIsActive(FlagConstant.NO);
        return bean;
    }

    private CommunityUserBean createCommunityUserBean(PersonalDetailForm personalDetail) {
        CommunityUserBean communityUserBean = new CommunityUserBean();
        communityUserBean.setUsername(personalDetail.getUsername());
        communityUserBean.setFullName(StringUtils.toRootUpperCase(personalDetail.getFullName()));
        communityUserBean.setContactNo(personalDetail.getContactNo());
        communityUserBean.setNric(personalDetail.getNric());
        communityUserBean.setGender(StringUtils.toRootUpperCase(personalDetail.getGender()));
        communityUserBean.setEthnic(StringUtils.toRootUpperCase(personalDetail.getEthnic()));
        communityUserBean.setEmail(StringUtils.isNotBlank(personalDetail.getEmail()) ? personalDetail.getEmail() : CommunityServiceCentreConstant.DEFAULT_EMAIL);
        return communityUserBean;
    }

    private AddressBean createAddressBean(String username, AddressForm addressForm) {
        AddressBean addressBean = new AddressBean();
        addressBean.setUsername(username);
        addressBean.setAddressLine1(StringUtils.toRootUpperCase(addressForm.getAddressLine1()));
        addressBean.setAddressLine2(StringUtils.toRootUpperCase(addressForm.getAddressLine2()));
        addressBean.setPostcode(addressForm.getPostcode());
        addressBean.setCity(StringUtils.toRootUpperCase(addressForm.getCity()));
        addressBean.setState(addressForm.getState());
        return addressBean;
    }

    private OccupationBean createOccupationBean(String username, OccupationForm occupationForm) {
        OccupationBean bean = new OccupationBean();
        bean.setUsername(username);
        bean.setEmploymentType(occupationForm.getOccupationType());
        bean.setOccupationName(StringUtils.toRootUpperCase(occupationForm.getOccupationName()));
        bean.setSalary(occupationForm.getSalary());
        if (isGovtEmployee(bean.getEmploymentType()) || isPrivateEmployee(bean.getEmploymentType())) {
            bean.setEmployerCompany(StringUtils.toRootUpperCase(occupationForm.getCompanyName()));
            bean.setEmployerContactNo(occupationForm.getCompanyContactNo());
        }

        return bean;
    }

    private boolean isPrivateEmployee(String employmentType) {
        return StringUtils.equals(employmentType, OccupationBean.EMPLOYMENT_TYPE_PRIVATE);
    }

    private boolean isGovtEmployee(String employmentType) {
        return StringUtils.equals(employmentType, OccupationBean.EMPLOYMENT_TYPE_GOVERNMENT);
    }
}
