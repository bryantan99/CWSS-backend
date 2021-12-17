package com.chis.communityhealthis.service.appointment;

import com.chis.communityhealthis.model.appointment.*;

import java.util.Date;
import java.util.List;

public interface AppointmentService {
    List<AppointmentModel> getPendingAppointments(Integer appointmentId);
    List<AppointmentModel> getLoggedInUserAppointments(String username, boolean currentLoggedInUserIsAdmin, Integer appointmentId, String status);
    void cancelAppointment(Integer appointmentId, String actionMakerUsername) throws Exception;
    AppointmentModel getAppointment(Integer appointmentId) throws Exception;
    void updateDatetime(UpdateDatetimeForm form) throws Exception;
    void confirmAppointment(ConfirmationForm form) throws Exception;
    Integer scheduleAppointment(ScheduleAppointmentForm form);
    List<AppointmentModel> getConfirmedAppointments(String username, boolean isAdmin, Date date);
    void updateAppointmentStatus(UpdateAppointmentStatusForm form) throws Exception;
}
