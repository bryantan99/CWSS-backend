package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.bean.AssistanceBean;
import com.chis.communityhealthis.model.assistance.AssistanceRecordTableModel;
import com.chis.communityhealthis.model.assistance.AssistanceRequestForm;
import com.chis.communityhealthis.model.assistance.AssistanceUpdateForm;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.service.auth.AuthService;
import com.chis.communityhealthis.service.assistance.AssistanceService;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/assistance")
public class AssistanceRestController {

    @Autowired
    private AssistanceService assistanceService;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/get-assistance-records", method = RequestMethod.GET)
    public ResponseEntity<List<AssistanceRecordTableModel>> getAssistanceRecords() {
        return new ResponseEntity<>(assistanceService.getAssistanceRecords(), HttpStatus.OK);
    }

    @GetMapping(value = "/current-user")
    public ResponseEntity<Object> findUserAssistanceRecords(@RequestParam String username) {
        try {
            List<AssistanceRecordTableModel> list = assistanceService.findUserAssistanceRecords(username);
            return ResponseHandler.generateResponse("Successfully searched user assistance records. Found " + (CollectionUtils.isEmpty(list) ? "0" : list.size() + " record(s)."), HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/new-request")
    public ResponseEntity<Object> addNewAssistance(@RequestBody AssistanceRequestForm form) {
        try {
            String currentUserUsername = authService.getCurrentLoggedInUsername();
            form.setCreatedBy(currentUserUsername);
            form.setUsername(StringUtils.isNotBlank(form.getUsername()) ? form.getUsername() : currentUserUsername);
            AssistanceBean bean = assistanceService.addAssistanceRequest(form);
            return ResponseHandler.generateResponse("Successfully added assistance.", HttpStatus.CREATED, bean);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping(value = "/{assistanceId}")
    public ResponseEntity<Object> deleteAssistance(@PathVariable Integer assistanceId) {
        String currentLoggedInUsername = authService.getCurrentLoggedInUsername();
        try {
            assistanceService.deleteAssistance(assistanceId, currentLoggedInUsername);
            return ResponseHandler.generateResponse("Successfully deleted assistance request ID: " + assistanceId.toString() + ".", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping(value = "/{assistanceId}/detail")
    public ResponseEntity<Object> getAssistanceRecordDetail(@PathVariable Integer assistanceId) {
        String currentLoggedInUsername = authService.getCurrentLoggedInUsername();
        try {
            return ResponseHandler.generateResponse("Successfully retrieved details of assistance request ID: " + assistanceId.toString() + ".", HttpStatus.OK, assistanceService.getAssistanceRecordDetail(assistanceId, currentLoggedInUsername));
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> findAllAssistanceRecords() {
        try {
            Assert.isTrue(authService.currentLoggedInUserIsAdmin(), "Unauthorized access.");
            List<AssistanceRecordTableModel> list = assistanceService.findAllAssistanceRecords();
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " assistance request record(s).", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/update")
    public ResponseEntity<Object> updateAssistanceRecord(@RequestBody AssistanceUpdateForm form) {
        try {
            form.setUpdatedBy(authService.getCurrentLoggedInUsername());
            form.setUpdatedDate(new Date());
            assistanceService.updateRecord(form);
            return ResponseHandler.generateResponse("Successfully updated assistance request [ID: " + form.getAssistanceId().toString() + "].", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
