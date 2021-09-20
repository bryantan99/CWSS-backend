package com.chis.communityhealthis.service;

import com.chis.communityhealthis.bean.AccountRoleBean;
import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.repository.accountRole.AccountRoleDao;
import com.chis.communityhealthis.repository.admin.AdminDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private AccountRoleDao accountRoleDao;

    @Override
    public String getCurrentLoggedInUsername() {
        String username = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            username = auth.getName();
        }

        return username;
    }

    @Override
    public Boolean currentLoggedInUserIsAdmin() {
        String currentLoggedInUsername = getCurrentLoggedInUsername();
        AdminBean adminBean = adminDao.find(currentLoggedInUsername);
        return adminBean != null;
    }

    @Override
    public Boolean hasRole(String roleName) {
        boolean hasRole = false;

        String currentLoggedInUsername = getCurrentLoggedInUsername();
        List<AccountRoleBean> roleBeanList = accountRoleDao.findUserRoles(currentLoggedInUsername);
        if (!CollectionUtils.isEmpty(roleBeanList)) {
            for (AccountRoleBean roleBean : roleBeanList) {
                if (StringUtils.equals(roleBean.getRoleName(), roleName)) {
                    hasRole = true;
                    break;
                }
            }
        }

        return hasRole;
    }
}
