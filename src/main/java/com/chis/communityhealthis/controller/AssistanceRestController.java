package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.assistance.AssistanceRecordTableModel;
import com.chis.communityhealthis.service.assistance.AssistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assistance")
public class AssistanceRestController {

    @Autowired
    private AssistanceService assistanceService;

    @RequestMapping(value = "/get-assistance-records", method = RequestMethod.GET)
    public ResponseEntity<List<AssistanceRecordTableModel>> getAssistanceRecords() {
        return new ResponseEntity<>(assistanceService.getAssistanceRecords(), HttpStatus.OK);
    }
}
