package com.chis.communityhealthis.service.auth;

public interface AuthService {
    String getCurrentLoggedInUsername();
    Boolean currentLoggedInUserIsAdmin();
    Boolean hasRole(String roleName);
}
