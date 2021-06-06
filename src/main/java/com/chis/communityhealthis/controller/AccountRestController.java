package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/account")
public class AccountRestController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/is-valid-username", method = RequestMethod.GET)
    public ResponseEntity<Boolean> isValidUsername(@RequestParam String username) {
        return new ResponseEntity<>(this.accountService.isValidUsername(username), HttpStatus.OK);
    }
}
