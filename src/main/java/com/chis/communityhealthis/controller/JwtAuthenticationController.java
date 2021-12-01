package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.jwt.JwtRequestModel;
import com.chis.communityhealthis.model.jwt.JwtResponseModel;
import com.chis.communityhealthis.model.account.AccountModel;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
import com.chis.communityhealthis.security.ChisUserDetailsService;
import com.chis.communityhealthis.service.account.AccountService;
import com.chis.communityhealthis.utility.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ChisUserDetailsService userDetailsService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody JwtRequestModel authenticationRequest) throws Exception {
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);
            accountService.updateLastLogin(userDetails.getUsername());
            return ResponseHandler.generateResponse("Successfully authenticated user.", HttpStatus.OK, new JwtResponseModel(userDetails, token));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException || e instanceof DisabledException) {
                return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.UNAUTHORIZED, null);
            } else {
                return ResponseHandler.generateResponse("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR, null);
            }
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Object> saveUser(@RequestBody AccountRegistrationForm accountRegistrationForm) throws Exception {
        try {
            AccountModel model = userDetailsService.createAccount(accountRegistrationForm);
            return ResponseHandler.generateResponse("Successfully created account.", HttpStatus.OK, model);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new DisabledException("User account has not been activated.", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username / password.", e);
        }
    }
}
