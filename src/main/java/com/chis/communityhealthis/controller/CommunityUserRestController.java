package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.filter.CommunityUserBeanQuery;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
import com.chis.communityhealthis.model.user.BlockDetailModel;
import com.chis.communityhealthis.model.user.BlockUserForm;
import com.chis.communityhealthis.model.user.CommunityUserModel;
import com.chis.communityhealthis.model.user.CommunityUserProfileModel;
import com.chis.communityhealthis.service.auth.AuthService;
import com.chis.communityhealthis.service.communityuser.CommunityUserService;
import com.chis.communityhealthis.utility.FlagConstant;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;
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
            if (e instanceof NotFoundException) {
                return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
            } else {
                return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            }
        }
    }

    @GetMapping
    public ResponseEntity<Object> getCommunityUsers(@RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String nric,
                                                    @RequestParam(required = false) String gender,
                                                    @RequestParam(required = false) String ethnic,
                                                    @RequestParam(required = false) Integer diseaseId,
                                                    @RequestParam(required = false) Integer zoneId) {
        try {
            CommunityUserBeanQuery filter = new CommunityUserBeanQuery();
            filter.setName(StringUtils.isBlank(name) ? null : name);
            filter.setNric(StringUtils.isBlank(nric) ? null : nric);
            filter.setGender(StringUtils.isBlank(gender) ? null : gender);
            filter.setEthnic(StringUtils.isBlank(ethnic) ? null : ethnic);
            filter.setDiseaseId(diseaseId);
            filter.setZoneId(zoneId);

            List<CommunityUserModel> list = communityUserService.getCommunityUsers(filter);
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " user record(s).", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
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
            String currentLoggedInUsername = authService.getCurrentLoggedInUsername();
            communityUserService.rejectUserAccount(username, currentLoggedInUsername);
            String msg = "Successfully rejected " + username + " profile.";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @RequestMapping(value = "/delete-user/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteUserAccount(@PathVariable String username) {
        try {
            String currentLoggedInUsername = authService.getCurrentLoggedInUsername();
            communityUserService.deleteUserAccount(username, currentLoggedInUsername);
            return ResponseHandler.generateResponse("Successfully deleted user account [username: " + username + "].", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @RequestMapping(value = "/update-user", method = RequestMethod.POST)
    public ResponseEntity<Object> updateUserAccount(@RequestBody AccountRegistrationForm form) {
        try {
            String actionMaker = authService.getCurrentLoggedInUsername();
            boolean isAdmin = authService.currentLoggedInUserIsAdmin();
            communityUserService.updateUserAccount(form, actionMaker, isAdmin);
            return ResponseHandler.generateResponse("Successfully updated user profile", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/block-user")
    public ResponseEntity<Object> blockUser(@RequestBody BlockUserForm form) {
        try {
            String currentLoggedInUsername = authService.getCurrentLoggedInUsername();
            boolean isAdmin = authService.currentLoggedInUserIsAdmin();
            if (!isAdmin) {
                throw new Exception("Unauthorized user.");
            }
            form.setActionBy(currentLoggedInUsername);
            communityUserService.blockUser(form);
            return ResponseHandler.generateResponse("Successfully blocked user [username: " + form.getUsername() +"]", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/unblock-user")
    public ResponseEntity<Object> unblockUser(@RequestBody BlockUserForm form) {
        try {
            String currentLoggedInUsername = authService.getCurrentLoggedInUsername();
            boolean isAdmin = authService.currentLoggedInUserIsAdmin();
            if (!isAdmin) {
                throw new Exception("Unauthorized user.");
            }
            form.setActionBy(currentLoggedInUsername);
            communityUserService.unblockUser(form);
            return ResponseHandler.generateResponse("Successfully unblocked user [username: " + form.getUsername() +"]", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping(value = "/validate-user-eligibility")
    public ResponseEntity<Object> validateIfUserIsEligibleToRequestAssistance(@RequestParam String username) {
        try {
            BlockDetailModel blockDetailModel = communityUserService.getBlockDetail(username);
            return ResponseHandler.generateResponse("User [username: " + username + "] is " + (blockDetailModel != null ? "blocked." : "not blocked."), HttpStatus.OK, blockDetailModel);
        } catch (Exception e) {
            BlockDetailModel blockDetailModel = new BlockDetailModel();
            blockDetailModel.setUsername(username);
            blockDetailModel.setIsBlocked(true);
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, blockDetailModel);
        }
    }
}
