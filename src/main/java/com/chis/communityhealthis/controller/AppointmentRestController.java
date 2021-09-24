package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.bean.AppointmentBean;
import com.chis.communityhealthis.model.appointment.ConfirmationForm;
import com.chis.communityhealthis.model.appointment.UpdateDatetimeForm;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.service.AuthService;
import com.chis.communityhealthis.service.appointment.AppointmentService;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/appointment")
public class AppointmentRestController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AuthService authService;

    @GetMapping(value = "/{appointmentId}")
    public ResponseEntity<Object> getAppointment(@PathVariable Integer appointmentId) {
        try {
            AppointmentBean appointmentBean = appointmentService.getAppointment(appointmentId);
            String msg = "Successfully retrieved appointment [ID: " + appointmentId.toString() + "].";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, appointmentBean);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllAppointments() {
        try {
            Assert.isTrue(authService.currentLoggedInUserIsAdmin(), "Unauthorized user.");

            List<AppointmentBean> appointmentBeanList = appointmentService.getAllAppointments();
            String msg = "Successfully retrieved " + appointmentBeanList.size() + " record(s).";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, appointmentBeanList);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUserAppointments(@RequestParam String username) {
        try {
            String currentLoggedInUser = authService.getCurrentLoggedInUsername();
            Assert.isTrue(StringUtils.equals(username, currentLoggedInUser), currentLoggedInUser + " is unauthorized to view " + username + " record(s).");

            List<AppointmentBean> appointmentBeanList = appointmentService.getUserAppointments(username);
            String msg = "Successfully retrieved " + appointmentBeanList.size() + " record(s).";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, appointmentBeanList);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping(value = "/{appointmentId}")
    public ResponseEntity<Object> cancelAppointment(@PathVariable Integer appointmentId) {
        try {
            String actionMakerUsername = authService.getCurrentLoggedInUsername();
            appointmentService.cancelAppointment(appointmentId, actionMakerUsername);
            String msg = "Successfully cancelled appointment [ID: " + appointmentId.toString() + "].";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/update-datetime")
    public ResponseEntity<Object> updateDatetime(@RequestBody UpdateDatetimeForm form) {
        try {
            form.setUpdatedBy(authService.getCurrentLoggedInUsername());
            form.setUpdatedDate(new Date());
            appointmentService.updateDatetime(form);
            String msg = "Successfully updated datetime for appointment [ID: " + form.getAppointmentId().toString() + "].";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<Object> confirmAppointment(@RequestBody ConfirmationForm form) {
        try {
            form.setConfirmedBy(authService.getCurrentLoggedInUsername());
            form.setConfirmedDate(new Date());
            appointmentService.confirmAppointment(form);
            String msg = "Successfully confirmed appointment [ID: " + form.getAppointmentId().toString() + "].";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

}
