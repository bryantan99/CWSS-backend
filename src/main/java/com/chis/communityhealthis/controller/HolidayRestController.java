package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.service.holiday.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/holiday")
public class HolidayRestController {

    @Autowired
    private HolidayService holidayService;

    @PostMapping(value = "/excel")
    public ResponseEntity<Object> insertRecordUsingExcel(@RequestParam(value = "file") MultipartFile file) {
        try {
            Integer numberOfRecordsInserted = holidayService.insertRecordsUsingExcel(file);
            return ResponseHandler.generateResponse("Successfully inserted " + numberOfRecordsInserted + " record(s).", HttpStatus.OK, numberOfRecordsInserted);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getHolidayDates(@RequestParam (required = false) String year) {
        try {
            List<Date> holidays = holidayService.getHolidayDates();
            return ResponseHandler.generateResponse("Successfully retrieved " + holidays.size() + " date(s).", HttpStatus.OK, holidays);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
