package com.chis.communityhealthis.service;

public interface AuthService {
    String getCurrentLoggedInUsername();
    Boolean currentLoggedInUserIsAdmin();
    Boolean hasRole(String roleName);
}
