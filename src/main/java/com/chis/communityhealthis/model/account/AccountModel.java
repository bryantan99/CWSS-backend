package com.chis.communityhealthis.model.account;

public class AccountModel {
    private String username;
    private String isActive;

    public AccountModel() {
    }

    public AccountModel(String username, String isActive) {
        this.username = username;
        this.isActive = isActive;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
