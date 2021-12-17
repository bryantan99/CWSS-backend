package com.chis.communityhealthis.repository.appointment;

import com.chis.communityhealthis.bean.AppointmentBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.Date;
import java.util.List;

public interface AppointmentDao extends GenericDao<AppointmentBean, Integer> {
    List<AppointmentBean> getUserAppointments(String username, boolean usernameIsAdmin, Integer appointmentId, String status);
    List<AppointmentBean> getAdminAppointments(String adminUsername, Date date);
    List<AppointmentBean> getConfirmedAppointments(String username, boolean isAdmin, Date date);
    List<AppointmentBean> getPendingAdminAppointmentsWithoutAdminUsername(Integer appointmentId);
    AppointmentBean findClashedAppointment(String username, boolean isAdmin, Date appointmentStartTime);
    List<AppointmentBean> getCommunityUserAppointments(String username, Date date);
}
