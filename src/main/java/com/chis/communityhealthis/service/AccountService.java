package com.chis.communityhealthis.service;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.model.signup.AccountRegistrationForm;

public interface AccountService {
    boolean addAccount(AccountRegistrationForm form);
    AccountBean getAccountWithRoles(String username);
}
