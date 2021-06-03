package com.chis.communityhealthis.repository;

import com.chis.communityhealthis.bean.AccountBean;

import java.util.Collection;
import java.util.List;

public interface AccountDao extends GenericDao<AccountBean, String> {
    AccountBean findAccount(String username);
    List<AccountBean> findAccounts (Collection<String> usernames);
    AccountBean findAccountWithRoles(String username);
}
