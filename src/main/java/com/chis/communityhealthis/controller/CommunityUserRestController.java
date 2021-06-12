package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
import com.chis.communityhealthis.model.user.CommunityUserProfileModel;
import com.chis.communityhealthis.model.user.CommunityUserTableModel;
import com.chis.communityhealthis.service.AuthService;
import com.chis.communityhealthis.service.communityuser.CommunityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community-user")
public class CommunityUserRestController {

    @Autowired
    private CommunityUserService communityUserService;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/view-profile", method = RequestMethod.GET)
    public ResponseEntity<CommunityUserProfileModel> getCommunityUser(@RequestParam String username) {
        return new ResponseEntity<>(communityUserService.getCommunityUserProfile(username), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-community-users", method = RequestMethod.GET)
    public ResponseEntity<List<CommunityUserTableModel>> getCommunityUsers() {
        return new ResponseEntity<>(communityUserService.getCommunityUsers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/approve-user", method = RequestMethod.GET)
    public ResponseEntity<String> approveUserAccount(@RequestParam String username) {
        communityUserService.approveUserAccount(username, authService.getCurrentLoggedInUsername());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete-user/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUserAccount(@PathVariable String username) {
        communityUserService.deleteUserAccount(username);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/update-user", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateUserAccount(@RequestBody AccountRegistrationForm form) {
        communityUserService.updateUserAccount(form);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
