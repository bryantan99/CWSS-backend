package com.chis.communityhealthis.repository;

import com.chis.communityhealthis.bean.AccountBean;

public interface AccountDao extends GenericDao<AccountBean, String> {
    AccountBean findAccount(String username);
    AccountBean findAccountWithRoles(String username);
}
