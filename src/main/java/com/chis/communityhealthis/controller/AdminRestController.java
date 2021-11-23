package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.model.signup.AdminForm;
import com.chis.communityhealthis.model.user.AdminDetailModel;
import com.chis.communityhealthis.service.admin.AdminService;
import com.chis.communityhealthis.service.auth.AuthService;
import com.chis.communityhealthis.utility.RoleConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Assert;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AdminRestController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthService authService;

    @GetMapping("/admins/profiles")
    public ResponseEntity<Object> getAdminsProfiles() {
        try {
            List<AdminDetailModel> list = adminService.findAllAdmins();
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " admin(s) profile.", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/admin/profile")
    public ResponseEntity<Object> getAdminProfile(@RequestParam String username) {
        try {
            AdminDetailModel adminDetailModel = adminService.getAdmin(username);
            return ResponseHandler.generateResponse("Successfully retrieved " + username + " profile.", HttpStatus.OK, adminDetailModel);
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
            } else {
                return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            }
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<Object> addStaff(@RequestParam(value = "form") String form,
                                           @RequestParam(value = "files", required = false)List<MultipartFile> multipartFileList) {
        try {
            AdminForm adminForm = new ObjectMapper().readValue(form, AdminForm.class);
            adminForm.setCreatedBy(authService.getCurrentLoggedInUsername());
            if (!CollectionUtils.isEmpty(multipartFileList)) {
                adminForm.setProfilePicFile(multipartFileList.get(0));
            }
            AdminBean adminBean = adminService.addStaff(adminForm);
            return ResponseHandler.generateResponse("Successfully created new staff [username: " + adminForm.getUsername() + "].", HttpStatus.OK, adminBean);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/admin/{username}")
    public ResponseEntity<Object> deleteStaff(@PathVariable String username) {
        try {
            String currentLoggedInUser = authService.getCurrentLoggedInUsername();
            Boolean isSuperAdmin = authService.hasRole(RoleConstant.SUPER_ADMIN);
            Assert.isTrue(isSuperAdmin, "Unauthorized user.");
            Assert.isTrue(!StringUtils.equals(currentLoggedInUser, username), "User is attempting to delete self account.");
            adminService.deleteStaff(username);
            return ResponseHandler.generateResponse("Successfully deleted admin [username: " + username + "]", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/admin/profile")
    public ResponseEntity<Object> updateAdmin(@RequestParam(value = "form") String form,
                                              @RequestParam(value = "files", required = false)List<MultipartFile> multipartFileList) {
        try {
            AdminForm adminForm = new ObjectMapper().readValue(form, AdminForm.class);
            adminForm.setCreatedBy(authService.getCurrentLoggedInUsername());
            if (!CollectionUtils.isEmpty(multipartFileList)) {
                adminForm.setProfilePicFile(multipartFileList.get(0));
            }
            adminService.updateAdmin(adminForm);
            String msg = "Successfully updated " + adminForm.getUsername() + " profile.";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

}
