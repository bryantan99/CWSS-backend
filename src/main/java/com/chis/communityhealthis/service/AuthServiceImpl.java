package com.chis.communityhealthis.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{

    @Override
    public String getCurrentLoggedInUsername() {
        String username = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if ( auth != null) {
            username = auth.getName();
        }

        return username;
    }
}
