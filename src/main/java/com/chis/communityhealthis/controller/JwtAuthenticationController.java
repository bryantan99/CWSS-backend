package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.JwtRequestModel;
import com.chis.communityhealthis.model.JwtResponseModel;
import com.chis.communityhealthis.model.account.AccountModel;
import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
import com.chis.communityhealthis.security.ChisUserDetailsService;
import com.chis.communityhealthis.service.AccountService;
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
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestModel authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        accountService.updateLastLogin(userDetails.getUsername());
        return ResponseEntity.ok(new JwtResponseModel(userDetails, token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<AccountModel> saveUser(@RequestBody AccountRegistrationForm accountRegistrationForm) throws Exception {
        return new ResponseEntity<>(userDetailsService.createAccount(accountRegistrationForm), HttpStatus.CREATED);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
