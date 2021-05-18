package com.chis.communityhealthis.service;

import com.chis.communityhealthis.model.AccountBean;

public interface AccountService {
    AccountBean addAccount(AccountBean accountBean);
    AccountBean getAccount(String username);
}
