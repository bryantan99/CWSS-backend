package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.account.ChangePasswordRequestModel;
import com.chis.communityhealthis.model.account.PasswordResetRequestModel;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.service.account.AccountService;
import com.chis.communityhealthis.service.auth.AuthService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountRestController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    @GetMapping(value = "/validation/username")
    public ResponseEntity<Object> isValidUsername(@RequestParam String username) {
        try {
            Boolean isValid = this.accountService.isValidUsername(username);
            return ResponseHandler.generateResponse(username + " is " + (isValid ? " valid." : " invalid."), HttpStatus.OK, isValid);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping(value = "/validation/email")
    public ResponseEntity<Object> isValidEmail(@RequestParam String email) {
        try {
            Boolean isValid = this.accountService.isValidEmail(email);
            return ResponseHandler.generateResponse(email + " is " + (isValid ? " valid." : " invalid."), HttpStatus.OK, isValid);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping(value = "/validation/nric")
    public ResponseEntity<Object> isUniqueNric(@RequestParam String nric) {
        try {
            Boolean isUnique = this.accountService.isUniqueNric(nric);
            return ResponseHandler.generateResponse(nric + (isUnique ? " is " : " is not ") + "unique.", HttpStatus.OK, isUnique);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/request-password-reset")
    public ResponseEntity<Object> requestPasswordReset(@RequestBody PasswordResetRequestModel model) {
        try {
            accountService.requestResetPassword(model.getUsername());
            return ResponseHandler.generateResponse("Successfully requested for password reset.", HttpStatus.OK, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @PostMapping(value = "/validation/otp")
    public ResponseEntity<Object> validateOtp(@RequestBody PasswordResetRequestModel model) {
        try {
            Boolean isValid = accountService.validateOtp(model);
            return ResponseHandler.generateResponse("Successfully validated OTP.", HttpStatus.OK, isValid);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody PasswordResetRequestModel model) {
        try {
            accountService.resetPassword(model);
            return ResponseHandler.generateResponse("Successfully reset password.", HttpStatus.OK, true);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ChangePasswordRequestModel model) {
        try {
            accountService.changePassword(model);
            return ResponseHandler.generateResponse("Successfully changed password.", HttpStatus.OK, true);
        } catch (Exception e) {
            String errorMessage;
            Map<String, Boolean> response = new HashMap<>();
            if (e instanceof BadCredentialsException) {
                errorMessage = "Incorrect old password.";
                response.put("incorrectPassword", true);
            } else {
                errorMessage = e.getMessage();
            }
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, MapUtils.isEmpty(response) ? null : response);
        }
    }

    @GetMapping(value = "/current-username")
    public ResponseEntity<Object> getCurrentUsername() {
        try {
            return ResponseHandler.generateResponse("Successfully identified logged in user.", HttpStatus.OK, authService.getCurrentLoggedInUsername());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
