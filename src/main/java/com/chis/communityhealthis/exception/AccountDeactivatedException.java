package com.chis.communityhealthis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountDeactivatedException extends RuntimeException {

    private static final long serialVersionUID = 2081930942633358198L;

    public AccountDeactivatedException(String username) {
        super(String.format("Account has been deactivated for [%s]: %s", username, "Account has been deactivated. Refresh token may also be revoked."));
    }
}
