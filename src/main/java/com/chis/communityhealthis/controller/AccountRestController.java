package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountRestController {

    @Autowired
    private AccountService accountService;

//    @RequestMapping(value = "/get-account", method = RequestMethod.GET)
//    public ResponseEntity<AccountBean> getAccount(@RequestParam String username) {
//        return new ResponseEntity<>(accountService.getAccount(username), HttpStatus.OK);
//    }

//    @RequestMapping(value = "/get-account-with-roles", method = RequestMethod.GET)
//    public ResponseEntity<AccountBean> getAccountWithRoles(@RequestParam String username) {
//        return new ResponseEntity<>(accountService.getAccountWithRoles(username), HttpStatus.OK);
//    }

}
