package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.holiday.HolidayForm;
import com.chis.communityhealthis.model.holiday.HolidayModel;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.service.holiday.HolidayService;
import com.chis.communityhealthis.service.storage.StorageService;
import com.chis.communityhealthis.utility.DirectoryConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/holiday")
public class HolidayRestController {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private StorageService storageService;

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

    @GetMapping("/year/{year}")
    public ResponseEntity<Object> getHolidayModelList(@PathVariable String year) {
        try {
            List<HolidayModel> holidays = holidayService.getHolidayModelList(year);
            return ResponseHandler.generateResponse("Successfully retrieved " + holidays.size() + " holiday(s).", HttpStatus.OK, holidays);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/id/{holidayId}")
    public ResponseEntity<Object> getHolidayModel(@PathVariable Integer holidayId) {
        try {
            HolidayModel holiday = holidayService.getHolidayModel(holidayId);
            return ResponseHandler.generateResponse("Successfully retrieved holiday.", HttpStatus.OK, holiday);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{holidayId}")
    public ResponseEntity<Object> deleteHoliday(@PathVariable Integer holidayId) {
        try {
            holidayService.deleteHoliday(holidayId);
            return ResponseHandler.generateResponse("Successfully deleted holiday.", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> addHoliday(@RequestBody HolidayForm form) {
        try {
            holidayService.addHoliday(form);
            return ResponseHandler.generateResponse("Successfully added holiday.", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateHoliday(@RequestBody HolidayForm form) {
        try {
            holidayService.updateHoliday(form);
            return ResponseHandler.generateResponse("Successfully updated holiday.", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/excel/template")
    public ResponseEntity<ByteArrayResource> downloadHolidayExcelTpl() {
        try {
            String fileName = "holiday";
            final String relativeToHolidayExcelFile = DirectoryConstant.AWS_HOLIDAY_DIRECTORY + "/holiday.xlsx";
            byte[] data = this.storageService.downloadFile(relativeToHolidayExcelFile);
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity.ok()
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                    .contentLength(data.length)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
