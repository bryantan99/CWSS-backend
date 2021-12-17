package com.chis.communityhealthis.factory;

import com.chis.communityhealthis.bean.AppointmentBean;
import com.chis.communityhealthis.model.appointment.AppointmentModel;

public class AppointmentModelFactory {

    public static AppointmentModel createAppointmentModel(AppointmentBean appointmentBean) {
        AppointmentModel model = new AppointmentModel();
        model.setAppointmentId(appointmentBean.getAppointmentId());
        model.setPurpose(appointmentBean.getAppointmentPurpose());
        model.setStartDatetime(appointmentBean.getAppointmentStartTime());
        model.setEndDatetime(appointmentBean.getAppointmentEndTime());
        model.setUsername(appointmentBean.getUsername());
        if (appointmentBean.getCommunityUserBean() != null) {
            model.setUserFullName(appointmentBean.getCommunityUserBean().getFullName());
        }
        model.setAdminUsername(appointmentBean.getAdminUsername());
        if (appointmentBean.getAdminBean() != null) {
            model.setAdminFullName(appointmentBean.getAdminBean().getFullName());
        }
        model.setStatus(appointmentBean.getAppointmentStatus());
        model.setLastUpdatedDate(appointmentBean.getLastUpdatedDate());
        return model;
    }
}
