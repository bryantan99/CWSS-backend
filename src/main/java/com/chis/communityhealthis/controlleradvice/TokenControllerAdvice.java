package com.chis.communityhealthis.controlleradvice;

import com.chis.communityhealthis.exception.TokenRefreshException;
import com.chis.communityhealthis.model.response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler(value = TokenRefreshException.class)
    public ResponseEntity<Object> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request.getDescription(false));
    }

}
