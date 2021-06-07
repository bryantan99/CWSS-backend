package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.dropdown.DropdownChoiceModel;
import com.chis.communityhealthis.service.dropdownChoice.DropdownChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dropdown")
public class DropdownChoiceRestController {

    @Autowired
    private DropdownChoiceService dropdownChoiceService;

    @RequestMapping(value = "/get-disease-choice-list", method = RequestMethod.GET)
    public ResponseEntity<List<DropdownChoiceModel>> getDiseaseChoiceList() {
        return new ResponseEntity<>(dropdownChoiceService.getDiseaseDropdownList(), HttpStatus.OK);
    }
}
