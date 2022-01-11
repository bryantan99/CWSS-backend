package com.chis.communityhealthis.model.user;

import java.util.List;

public class LoggedInUser {
    private String username;
    private List<String> roleList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }
}
