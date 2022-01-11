package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.audit.AuditModel;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.model.user.LoggedInUser;
import com.chis.communityhealthis.service.audit.AuditService;
import com.chis.communityhealthis.service.auth.AuthService;
import com.chis.communityhealthis.utility.RoleConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditRestController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<Object> getAuditLogs(@RequestParam (required = false) String moduleName) {
        try {
            LoggedInUser user = authService.getCurrentLoggedInUser();
            if (!user.getRoleList().contains(RoleConstant.SUPER_ADMIN)) {throw new Exception("Unauthorized user.");}
            List<AuditModel> list = auditService.getAuditLogs(moduleName);
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " audit logs.", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
