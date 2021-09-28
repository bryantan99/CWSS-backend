package com.chis.communityhealthis.model.jwt;

import java.io.Serializable;

public class JwtRequestModel implements Serializable {

    private static final long serialVersionUID = -4419833669671543220L;

    private String username;
    private String password;

    public JwtRequestModel() {
    }

    public JwtRequestModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
