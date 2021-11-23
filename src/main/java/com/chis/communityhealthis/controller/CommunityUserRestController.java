package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.filter.CommunityUserBeanQuery;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
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
                                                    @RequestParam(required = false) Integer zoneId,
                                                    @RequestParam(required = false) String address,
                                                    @RequestParam(required = false) String occupation,
                                                    @RequestParam(required = false) String healthIssue) {
        try {
            CommunityUserBeanQuery filter = new CommunityUserBeanQuery();
            filter.setName(StringUtils.isBlank(name) ? null : name);
            filter.setNric(StringUtils.isBlank(nric) ? null : nric);
            filter.setGender(StringUtils.isBlank(gender) ? null : gender);
            filter.setEthnic(StringUtils.isBlank(ethnic) ? null : ethnic);
            filter.setDiseaseId(diseaseId);
            filter.setZoneId(zoneId);
            filter.setIncludeAddress(!StringUtils.isBlank(address) && StringUtils.equals(FlagConstant.YES, address));
            filter.setIncludeOccupation(!StringUtils.isBlank(occupation) && StringUtils.equals(FlagConstant.YES, occupation));
            filter.setIncludeHealthIssue(!StringUtils.isBlank(healthIssue) && StringUtils.equals(FlagConstant.YES, healthIssue));

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
    public ResponseEntity<Object> updateUserAccount(@RequestBody AccountRegistrationForm form) {
        try {
            communityUserService.updateUserAccount(form);
            return ResponseHandler.generateResponse("Successfully updated user profile", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
