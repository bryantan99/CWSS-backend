package com.chis.communityhealthis.service;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.model.account.AccountModel;
import com.chis.communityhealthis.model.account.PasswordResetRequestModel;
import com.chis.communityhealthis.model.signup.AccountRegistrationForm;

public interface AccountService {
    AccountModel addAccount(AccountRegistrationForm form);
    AccountBean getAccountWithRoles(String username);
    Boolean isValidUsername(String username);
    void updateLastLogin(String username);
    void requestResetPassword(String email);
    Boolean validateOtp(PasswordResetRequestModel model);
    void resetPassword(PasswordResetRequestModel model);
}
