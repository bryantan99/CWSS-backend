package com.chis.communityhealthis;

import com.chis.communityhealthis.model.AccountBean;
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

    @RequestMapping(value = "/add-account", method = RequestMethod.POST)
    public ResponseEntity<AccountBean> addAccount(@RequestBody AccountBean accountBean) {
        return new ResponseEntity<>(accountService.addAccount(accountBean), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/get-account", method = RequestMethod.GET)
    public ResponseEntity<AccountBean> getAccount(@RequestParam String username) {
        return new ResponseEntity<>(accountService.getAccount(username), HttpStatus.OK);
    }

}
