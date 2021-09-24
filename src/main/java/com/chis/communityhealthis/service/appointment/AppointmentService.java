package com.chis.communityhealthis.service.appointment;

import com.chis.communityhealthis.bean.AppointmentBean;
import com.chis.communityhealthis.model.appointment.ConfirmationForm;
import com.chis.communityhealthis.model.appointment.UpdateDatetimeForm;

import java.util.List;

public interface AppointmentService {
    List<AppointmentBean> getAllAppointments();
    List<AppointmentBean> getUserAppointments(String username);
    void cancelAppointment(Integer appointmentId, String actionMakerUsername) throws Exception;
    AppointmentBean getAppointment(Integer appointmentId);
    void updateDatetime(UpdateDatetimeForm form);
    void confirmAppointment(ConfirmationForm form);
}
