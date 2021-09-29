package com.chis.communityhealthis.repository.account;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.Collection;
import java.util.List;

public interface AccountDao extends GenericDao<AccountBean, String> {
    AccountBean findAccount(String username);
    List<AccountBean> findAccounts (Collection<String> usernames);
    AccountBean findAccountWithRoles(String username);
    AccountBean findAccountByEmail(String email);
}
