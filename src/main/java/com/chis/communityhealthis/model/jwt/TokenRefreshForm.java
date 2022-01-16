package com.chis.communityhealthis.model.jwt;

import javax.validation.constraints.NotBlank;

public class TokenRefreshForm {
    @NotBlank
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
