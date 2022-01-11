package com.chis.communityhealthis.service.auth;

import com.chis.communityhealthis.model.user.LoggedInUser;

public interface AuthService {
    String getCurrentLoggedInUsername();
    Boolean currentLoggedInUserIsAdmin();
    Boolean hasRole(String roleName);
    LoggedInUser getCurrentLoggedInUser();
}
