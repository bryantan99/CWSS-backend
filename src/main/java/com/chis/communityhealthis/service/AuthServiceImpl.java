package com.chis.communityhealthis.service;

import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.repository.admin.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{

    @Autowired
    private AdminDao adminDao;

    @Override
    public String getCurrentLoggedInUsername() {
        String username = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if ( auth != null) {
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
}
