package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.bean.AssistanceBean;
import com.chis.communityhealthis.model.assistance.*;
import com.chis.communityhealthis.model.assistancecategory.AssistanceCategoryForm;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.service.assistance.AssistanceService;
import com.chis.communityhealthis.service.auth.AuthService;
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

    @GetMapping(value = "/pending")
    public ResponseEntity<Object> getPendingAssistanceRecords(@RequestParam(required = false) Integer assistanceId) {
        try {
            List<AssistanceModel> list = assistanceService.getPendingAssistanceRecords(assistanceId);
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " assistance request(s).", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping(value = "/current-user")
    public ResponseEntity<Object> findUserAssistanceRecords(@RequestParam(required = false) String username,
                                                            @RequestParam(required = false) Integer assistanceId,
                                                            @RequestParam(required = false) String title,
                                                            @RequestParam(required = false) String status) {
        try {
            if (StringUtils.isBlank(username)) {
                username = authService.getCurrentLoggedInUsername();
            }
            AssistanceQueryForm queryForm = initQueryForm(assistanceId, null, title, status, null, username, null);
            List<AssistanceModel> list = assistanceService.findUserAssistanceRecords(queryForm);
            return ResponseHandler.generateResponse("Successfully searched " + list.size() + " assistance records.", HttpStatus.OK, list);
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
            AssistanceModel assistanceModel = assistanceService.getAssistanceRecordDetail(assistanceId, currentLoggedInUsername);
            return ResponseHandler.generateResponse("Successfully retrieved details of assistance request ID: " + assistanceId.toString() + ".", HttpStatus.OK, assistanceModel);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping
    public ResponseEntity<Object> findAllAssistanceRecords(@RequestParam(required = false) Integer assistanceId,
                                                           @RequestParam(required = false) Integer categoryId,
                                                           @RequestParam(required = false) String title,
                                                           @RequestParam(required = false) String status,
                                                           @RequestParam(required = false) String nric,
                                                           @RequestParam(required = false) String adminUsername) {
        try {
            Assert.isTrue(authService.currentLoggedInUserIsAdmin(), "Unauthorized access.");
            AssistanceQueryForm form = initQueryForm(assistanceId, categoryId, title, status, nric, null, adminUsername);
            List<AssistanceModel> list = assistanceService.findAllAssistanceRecords(form);
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " assistance request record(s).", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/handled")
    public ResponseEntity<Object> getAdminHandledAssistanceRecords(@RequestParam(required = false) String adminUsername,
                                                                   @RequestParam(required = false) Integer assistanceId,
                                                                   @RequestParam(required = false) String title,
                                                                   @RequestParam(required = false) String status,
                                                                   @RequestParam(required = false) String username) {
        try {
            String currentLoginUsername = authService.getCurrentLoggedInUsername();
            boolean isAdmin = authService.currentLoggedInUserIsAdmin();
            if (!isAdmin) {throw new Exception("Unauthorized user.");}
            adminUsername = StringUtils.isBlank(adminUsername) ? currentLoginUsername : adminUsername;
            AssistanceQueryForm queryForm = initQueryForm(assistanceId, null, title, status, null, username, adminUsername);

            List<AssistanceModel> list = assistanceService.getAdminHandledAssistanceRecords(queryForm);
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

    @PostMapping(value = "/accept")
    public ResponseEntity<Object> acceptAssistanceRequest(@RequestBody AssistanceUpdateForm form) {
        try {
            if (!authService.currentLoggedInUserIsAdmin()) {
                throw new Exception("Unauthorized user.");
            }
            form.setPersonInCharge(authService.getCurrentLoggedInUsername());
            form.setUpdatedDate(new Date());
            form.setUpdatedBy(form.getPersonInCharge());
            assistanceService.acceptAssistanceRequest(form);
            return ResponseHandler.generateResponse("Successfully updated assistance request [ID: " + form.getAssistanceId() + "].", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/reject")
    public ResponseEntity<Object> rejectAssistanceRequest(@RequestBody AssistanceRejectForm form) {
        try {
            if (!authService.currentLoggedInUserIsAdmin()) {
                throw new Exception("Unauthorized user.");
            }
            form.setRejectedBy(authService.getCurrentLoggedInUsername());
            form.setRejectedDate(new Date());
            assistanceService.rejectAssistanceForm(form);
            return ResponseHandler.generateResponse("Successfully updated assistance request [ID: " + form.getAssistanceId() + "].", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping(value = "/category/{categoryId}")
    public ResponseEntity<Object> deleteAssistanceCategory(@PathVariable Integer categoryId) {
        try {
            if (!authService.currentLoggedInUserIsAdmin()) {
                throw new Exception("Unauthorized user.");
            }
            assistanceService.deleteCategory(categoryId);
            return ResponseHandler.generateResponse("Successfully deleted category [ID: " + categoryId  + "].", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/category")
    public ResponseEntity<Object> addAssistanceCategory(@RequestBody AssistanceCategoryForm form) {
        try {
            if (!authService.currentLoggedInUserIsAdmin()) {
                throw new Exception("Unauthorized user.");
            }
            Integer categoryId = assistanceService.addCategory(form);
            return ResponseHandler.generateResponse("Successfully added category [ID: " + categoryId  + "].", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/category/update")
    public ResponseEntity<Object> updateAssistanceCategory(@RequestBody AssistanceCategoryForm form) {
        try {
            if (!authService.currentLoggedInUserIsAdmin()) {
                throw new Exception("Unauthorized user.");
            }
            assistanceService.updateCategory(form);
            return ResponseHandler.generateResponse("Successfully updated category [ID: " + form.getCategoryId()  + "].", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    private AssistanceQueryForm initQueryForm(Integer assistanceId, Integer categoryId, String title, String status, String nric, String username, String adminUsername) {
        AssistanceQueryForm form = new AssistanceQueryForm();
        form.setAssistanceId(assistanceId);
        form.setCategoryId(categoryId);
        form.setTitle(title);
        form.setStatus(status);
        form.setNric(nric);
        form.setUsername(username);
        form.setAdminUsername(adminUsername);
        return form;
    }
}
