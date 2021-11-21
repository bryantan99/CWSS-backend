package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.dropdown.DropdownChoiceModel;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.service.dropdownchoice.DropdownChoiceService;
import com.chis.communityhealthis.utility.FlagConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/dropdown")
public class DropdownChoiceRestController {

    @Autowired
    private DropdownChoiceService dropdownChoiceService;

    @RequestMapping(value = "/get-disease-choice-list", method = RequestMethod.GET)
    public ResponseEntity<Object> getDiseaseChoiceList() {
        try {
            List<DropdownChoiceModel<String>> list = dropdownChoiceService.getDiseaseDropdownList();
            return ResponseHandler.generateResponse("Successfully retrieved" + list.size() + " choice(s).", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ResponseEntity<Object> getAdminUsernameList() {
        try {
            List<DropdownChoiceModel<String>> list = dropdownChoiceService.getAdminUsernameList();
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " choice(s).", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, new ArrayList<DropdownChoiceModel<String>>());
        }
    }

    @RequestMapping(value = "/community-user", method = RequestMethod.GET)
    public ResponseEntity<Object> getCommunityUserUsernameList(@RequestParam String hasNric) {
        try {
            List<DropdownChoiceModel<String>> list = dropdownChoiceService.getCommunityUserUsernameList(StringUtils.equals(FlagConstant.YES, hasNric));
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " choice(s).", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, new ArrayList<DropdownChoiceModel<String>>());
        }
    }

    @RequestMapping(value = "/appointment/timeslot", method = RequestMethod.GET)
    public ResponseEntity<Object> getAppointmentAvailableTimeslot(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date date,
                                                                  @RequestParam(required = false) String adminUsername) {
        try {
            List<DropdownChoiceModel<Date>> list = dropdownChoiceService.getAppointmentAvailableTimeslot(date, adminUsername);
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " choice(s).", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, new ArrayList<DropdownChoiceModel<Date>>());
        }
    }

    @GetMapping(value = "/zoneId")
    public ResponseEntity<Object> getZoneIdDropdownChoices() {
        try {
            List<DropdownChoiceModel<String>> list = dropdownChoiceService.getZoneIdsList();
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " choice(s).", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping(value = "/assistance-category")
    public ResponseEntity<Object> getAssistanceCategoryDropdownChoices() {
        try {
            List<DropdownChoiceModel<String>> list = dropdownChoiceService.getAssistanceCategoryDropdownChoices();
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " choice(s).", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
