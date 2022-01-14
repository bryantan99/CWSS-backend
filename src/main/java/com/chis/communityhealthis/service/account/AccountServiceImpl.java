package com.chis.communityhealthis.service.account;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.model.account.AccountModel;
import com.chis.communityhealthis.model.account.ChangePasswordRequestModel;
import com.chis.communityhealthis.model.account.PasswordResetRequestModel;
import com.chis.communityhealthis.model.signup.*;
import com.chis.communityhealthis.repository.account.AccountDao;
import com.chis.communityhealthis.repository.address.AddressDao;
import com.chis.communityhealthis.repository.communityuser.CommunityUserDao;
import com.chis.communityhealthis.repository.healthissue.HealthIssueDao;
import com.chis.communityhealthis.repository.occupation.OccupationDao;
import com.chis.communityhealthis.repository.resetpasswordrequest.ResetPasswordRequestDao;
import com.chis.communityhealthis.service.email.EmailService;
import com.chis.communityhealthis.service.sms.SmsService;
import com.chis.communityhealthis.utility.FlagConstant;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

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
    private ResetPasswordRequestDao resetPasswordRequestDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AccountModel addAccount(AccountRegistrationForm form) {
        AccountBean accountBean = createAccountBean(form.getPersonalDetail());
        String username = accountDao.add(accountBean);

        CommunityUserBean communityUserBean = createCommunityUserBean(form.getPersonalDetail());
        communityUserDao.add(communityUserBean);

        AddressBean addressBean = createAddressBean(username, form.getAddress());
        addressDao.add(addressBean);

        if (form.getOccupation() != null) {
            OccupationBean occupationBean = createOccupationBean(username, form.getOccupation());
            occupationDao.add(occupationBean);
        }

        if (form.getHealth() != null && !CollectionUtils.isEmpty(form.getHealth().getDiseaseList())) {
            for (HealthIssueModel diseaseModel : form.getHealth().getDiseaseList()) {
                HealthIssueBean healthIssueBean = createHealthIssueBean(username, diseaseModel);
                healthIssueDao.add(healthIssueBean);
            }
        }

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

    @Override
    public Boolean isValidEmail(String email) {
        AccountBean accountBean = accountDao.findAccountByEmail(email);
        return accountBean == null;
    }

    @Override
    public void updateLastLogin(String username) {
        AccountBean accountBean = accountDao.find(username);
        Assert.notNull(accountBean, "Account with username: " + username + " is not found.");
        accountBean.setLastLoginDate(new Date());
        accountDao.update(accountBean);
    }

    @Override
    public void requestResetPassword(String username) throws Exception {
        AccountBean accountBean = accountDao.findAccount(username);
        Assert.notNull(accountBean, "Account [username : " + username + "] was not found!");

        ResetPasswordRequestBean bean = new ResetPasswordRequestBean();
        bean.setUsername(accountBean.getUsername());
        bean.setOtp(RandomStringUtils.randomNumeric(6, 7));

        Calendar currentTime = Calendar.getInstance();
        currentTime.add(Calendar.MINUTE, 15);
        Date expiryDate = currentTime.getTime();
        bean.setOtpExpiryDate(expiryDate);
        resetPasswordRequestDao.add(bean);

        CommunityUserBean communityUserBean = communityUserDao.getCommunityUser(username);
        smsService.sendSms(communityUserBean.getContactNo(), "You've request for password reset. Your OTP code is " + bean.getOtp() + ".");
    }

    @Override
    public Boolean validateOtp(PasswordResetRequestModel model) {
        AccountBean accountBean = accountDao.findAccount(model.getUsername());
        Assert.notNull(accountBean, "Account [username : " + model.getUsername() + "] was not found.");

        ResetPasswordRequestBean passwordRequestBean = resetPasswordRequestDao.find(accountBean.getUsername());
        Assert.notNull(passwordRequestBean, "User with username : " + accountBean.getUsername() + " has not requested password reset.");

        Date currentDatetime = new Date();

        return model.getOtp().equals(passwordRequestBean.getOtp()) && currentDatetime.before(passwordRequestBean.getOtpExpiryDate());
    }

    @Override
    public void resetPassword(PasswordResetRequestModel model) {
        Assert.isTrue(StringUtils.equals(model.getPassword(), model.getConfirmPassword()), "Password and confirm password are mismatched.");

        AccountBean accountBean = accountDao.findAccount(model.getUsername());
        Assert.notNull(accountBean, "Account [username : " + model.getUsername() + "] was not found.");

        String encryptedPw = bCryptPasswordEncoder.encode(model.getPassword());
        accountBean.setPw(encryptedPw);
        accountDao.update(accountBean);

        ResetPasswordRequestBean resetPasswordRequestBean = resetPasswordRequestDao.find(accountBean.getUsername());
        resetPasswordRequestDao.remove(resetPasswordRequestBean);
    }

    @Override
    public void changePassword(ChangePasswordRequestModel model) {
        AccountBean accountBean = accountDao.findAccount(model.getUsername());
        Assert.notNull(accountBean, "Account [username: " + model.getUsername() + "] was not found!");

        if (!passwordEncoder.matches(model.getOldPassword(), accountBean.getPw())) {
            throw new BadCredentialsException("Old password is incorrect!");
        }

        Assert.isTrue(StringUtils.equals(model.getNewPassword(), model.getConfirmPassword()), "New password and confirm password are mismatched.");
        String newEncryptedPw = bCryptPasswordEncoder.encode(model.getNewPassword());
        accountBean.setPw(newEncryptedPw);
        accountDao.update(accountBean);
    }

    @Override
    public Boolean isUniqueNric(String nric) {
        CommunityUserBean userBean = communityUserDao.getCommunityUserByNric(nric);
        return userBean == null;
    }

    private AccountBean createAccountBean(PersonalDetailForm personalDetail) {
        Assert.isTrue(StringUtils.equals(personalDetail.getPassword(), personalDetail.getConfirmPassword()), "Password does not matched");
        AccountBean bean = new AccountBean();
        bean.setUsername(personalDetail.getUsername());
        bean.setPw(bCryptPasswordEncoder.encode(personalDetail.getPassword()));
        bean.setIsActive(FlagConstant.NO);
        if (StringUtils.isNotBlank(personalDetail.getEmail())) {
            bean.setEmail(personalDetail.getEmail());
        }
        return bean;
    }

    private CommunityUserBean createCommunityUserBean(PersonalDetailForm personalDetail) {
        CommunityUserBean communityUserBean = new CommunityUserBean();
        communityUserBean.setUsername(personalDetail.getUsername());
        communityUserBean.setFullName(StringUtils.toRootUpperCase(personalDetail.getFullName()));
        communityUserBean.setContactNo(personalDetail.getContactNo());
        communityUserBean.setNric(personalDetail.getNric());
        if (StringUtils.isNotBlank(personalDetail.getGender())) {
            communityUserBean.setGender(StringUtils.toRootUpperCase(personalDetail.getGender()));
        }
        if (StringUtils.isNotBlank(personalDetail.getEthnic())) {
            communityUserBean.setEthnic(StringUtils.toRootUpperCase(personalDetail.getEthnic()));
        }
        return communityUserBean;
    }

    private AddressBean createAddressBean(String username, AddressForm addressForm) {
        AddressBean addressBean = new AddressBean();
        addressBean.setUsername(username);
        addressBean.setAddressLine1(addressForm.getAddressLine1());
        addressBean.setAddressLine2(addressForm.getAddressLine2());
        addressBean.setPostcode(addressForm.getPostcode());
        addressBean.setCity(addressForm.getCity());
        addressBean.setState(addressForm.getState());
        addressBean.setZoneId(addressForm.getZoneId());
        return addressBean;
    }

    private OccupationBean createOccupationBean(String username, OccupationForm occupationForm) {
        OccupationBean bean = new OccupationBean();
        bean.setUsername(username);
        bean.setEmploymentType(occupationForm.getEmploymentType());
        bean.setOccupationName(StringUtils.toRootUpperCase(occupationForm.getOccupationName()));
        bean.setSalary(occupationForm.getSalary());
        if (isGovtEmployee(bean.getEmploymentType()) || isPrivateEmployee(bean.getEmploymentType())) {
            bean.setEmployerCompany(StringUtils.toRootUpperCase(occupationForm.getEmployerCompany()));
            bean.setEmployerContactNo(occupationForm.getEmployerContactNo());
        }

        return bean;
    }

    private HealthIssueBean createHealthIssueBean(String username, HealthIssueModel diseaseModel) {
        HealthIssueBean bean = new HealthIssueBean();
        bean.setUsername(username);
        bean.setDiseaseId(diseaseModel.getDiseaseId());
        bean.setIssueDescription(diseaseModel.getDescription());
        bean.setCreatedBy(username);
        bean.setCreatedDate(new Date());
        return bean;
    }

    private boolean isPrivateEmployee(String employmentType) {
        return StringUtils.equals(employmentType, OccupationBean.EMPLOYMENT_TYPE_PRIVATE);
    }

    private boolean isGovtEmployee(String employmentType) {
        return StringUtils.equals(employmentType, OccupationBean.EMPLOYMENT_TYPE_GOVERNMENT);
    }
}
