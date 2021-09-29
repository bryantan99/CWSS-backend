package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
import com.chis.communityhealthis.model.user.CommunityUserProfileModel;
import com.chis.communityhealthis.model.user.CommunityUserTableModel;
import com.chis.communityhealthis.service.auth.AuthService;
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

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResponseEntity<Object> getCommunityUser(@RequestParam String username) {
        try {
            CommunityUserProfileModel model = communityUserService.getCommunityUserProfile(username);
            return ResponseHandler.generateResponse("Successfully retrieved " + username + " profile.", HttpStatus.OK, model);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @RequestMapping(value = "/get-community-users", method = RequestMethod.GET)
    public ResponseEntity<List<CommunityUserTableModel>> getCommunityUsers() {
        return new ResponseEntity<>(communityUserService.getCommunityUsers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/approve-user", method = RequestMethod.GET)
    public ResponseEntity<Object> approveUserAccount(@RequestParam String username) {
        try {
            communityUserService.approveUserAccount(username, authService.getCurrentLoggedInUsername());
            String msg = "Successfully approved " + username + " profile.";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @RequestMapping(value = "/reject-user", method = RequestMethod.GET)
    public ResponseEntity<Object> rejectUserAccount(@RequestParam String username) {
        try {
            communityUserService.rejectUserAccount(username);
            String msg = "Successfully rejected " + username + " profile.";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
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
