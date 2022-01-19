package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.bean.RefreshTokenBean;
import com.chis.communityhealthis.model.account.AccountModel;
import com.chis.communityhealthis.model.jwt.*;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
import com.chis.communityhealthis.security.ChisUserDetailsService;
import com.chis.communityhealthis.security.UserDetailsModel;
import com.chis.communityhealthis.service.account.AccountService;
import com.chis.communityhealthis.service.refreshtoken.RefreshTokenService;
import com.chis.communityhealthis.utility.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ChisUserDetailsService userDetailsService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody JwtRequestModel authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetailsModel userDetails = (UserDetailsModel) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(userDetails);
            RefreshTokenBean refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

            accountService.updateLastLogin(userDetails.getUsername());
            return ResponseHandler.generateResponse("Successfully authenticated user.", HttpStatus.OK, new JwtResponseModel(userDetails, jwt, refreshToken.getToken()));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException || e instanceof DisabledException) {
                String msg = e instanceof BadCredentialsException ? "Incorrect username / password." : "User account has not been activated.";
                return ResponseHandler.generateResponse(msg, HttpStatus.UNAUTHORIZED, null);
            } else {
                return ResponseHandler.generateResponse("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR, null);
            }
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Object> saveUser(@RequestBody AccountRegistrationForm accountRegistrationForm) {
        try {
            AccountModel model = userDetailsService.createAccount(accountRegistrationForm);
            return ResponseHandler.generateResponse("Successfully created account.", HttpStatus.OK, model);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@Valid @RequestBody TokenRefreshForm request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshTokenBean refreshTokenBean = refreshTokenService.getByToken(requestRefreshToken);
        refreshTokenBean = refreshTokenService.verifyExpiration(refreshTokenBean);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(refreshTokenBean.getUsername());
        String token = jwtUtils.generateToken(userDetails);
        return ResponseHandler.generateResponse("Successfully refresh token.", HttpStatus.OK, new TokenRefreshResponse(token, requestRefreshToken));
    }
}
