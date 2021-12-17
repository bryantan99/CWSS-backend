package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.appointment.*;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.service.appointment.AppointmentService;
import com.chis.communityhealthis.service.auth.AuthService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
            AppointmentModel appointment = appointmentService.getAppointment(appointmentId);
            String msg = "Successfully retrieved appointment [ID: " + appointmentId.toString() + "].";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, appointment);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping(value = "/pending")
    public ResponseEntity<Object> getPendingAppointments(@RequestParam(required = false) Integer appointmentId) {
        try {
            Assert.isTrue(authService.currentLoggedInUserIsAdmin(), "Unauthorized user.");
            List<AppointmentModel> appointmentBeanList = appointmentService.getPendingAppointments(appointmentId);
            String msg = "Successfully retrieved " + appointmentBeanList.size() + " record(s).";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, appointmentBeanList);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getLoggedInUserAppointments(@RequestParam(required = false) Integer appointmentId,
                                                              @RequestParam(required = false) String status) {
        try {
            String username = authService.getCurrentLoggedInUsername();
            boolean currentLoggedInUserIsAdmin = authService.currentLoggedInUserIsAdmin();

            List<AppointmentModel> appointmentBeanList = appointmentService.getLoggedInUserAppointments(username, currentLoggedInUserIsAdmin, appointmentId, status);
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

    @PostMapping
    public ResponseEntity<Object> scheduleAppointment(@RequestBody ScheduleAppointmentForm form) {
        try {
            String actionMakerUsername = authService.getCurrentLoggedInUsername();
            form.setCreatedBy(actionMakerUsername);
            Integer appointmentId = appointmentService.scheduleAppointment(form);
            String msg = "Successfully scheduled appointment [ID: " + appointmentId + "].";
            return ResponseHandler.generateResponse(msg, HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping(value = "/upcoming")
    public ResponseEntity<Object> getConfirmedAppointments(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date date) {
        try {
            String username = authService.getCurrentLoggedInUsername();
            boolean isAdmin = authService.currentLoggedInUserIsAdmin();

            List<AppointmentModel> upcomingAppointmentStringList = appointmentService.getConfirmedAppointments(username, isAdmin, date);
            return ResponseHandler.generateResponse("Successfully retrieved " + upcomingAppointmentStringList.size() + " record(s).", HttpStatus.OK, upcomingAppointmentStringList);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/update-status")
    public ResponseEntity<Object> updateAppointmentStatus(@RequestBody UpdateAppointmentStatusForm form) {
        try {
            form.setSubmittedBy(authService.getCurrentLoggedInUsername());
            form.setSubmittedDate(new Date());
            appointmentService.updateAppointmentStatus(form);
            return ResponseHandler.generateResponse("Successfully updated appointment's status.", HttpStatus.OK, null);
        }catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
