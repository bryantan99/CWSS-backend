package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.signup.AdminForm;
import com.chis.communityhealthis.model.user.CommunityUserTableModel;
import com.chis.communityhealthis.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminRestController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admins/profiles")
    public ResponseEntity<List<CommunityUserTableModel>> getAdminsProfiles() {
        return new ResponseEntity<>(adminService.findAllAdmins(), HttpStatus.OK);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> addStaff(@RequestBody AdminForm form) {
        return new ResponseEntity<>(adminService.addStaff(form), HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/{username}")
    public ResponseEntity<?> deleteStaff(@PathVariable String username) {
        adminService.deleteStaff(username);
        return ResponseEntity.noContent().build();
    }

}
