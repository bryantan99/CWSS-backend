package com.chis.communityhealthis.service;

import com.chis.communityhealthis.bean.AccountBean;

public interface AccountService {
    void addAccount(AccountBean accountBean);
    AccountBean getAccount(String username);
    AccountBean getAccountWithRoles(String username);
}
