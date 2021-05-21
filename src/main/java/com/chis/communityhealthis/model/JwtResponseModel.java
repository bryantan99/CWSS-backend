package com.chis.communityhealthis.model;

import java.io.Serializable;

public class JwtResponseModel implements Serializable {

    private static final long serialVersionUID = 8256229639433822362L;
    private final String jwtToken;

    public JwtResponseModel(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
