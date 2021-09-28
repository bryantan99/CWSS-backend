package com.chis.communityhealthis.repository.accountrole;

import com.chis.communityhealthis.bean.AccountRoleBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface AccountRoleDao extends GenericDao<AccountRoleBean, Integer> {
    List<AccountRoleBean> findUserRoles(String username);
}
