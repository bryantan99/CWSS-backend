package com.chis.communityhealthis.model.appointment;

import java.util.Date;

public class ConfirmationForm {
    private Integer appointmentId;
    private Date appointmentLastUpdatedDate;
    private String confirmedBy;
    private Date confirmedDate;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getAppointmentLastUpdatedDate() {return appointmentLastUpdatedDate;}

    public void setAppointmentLastUpdatedDate(Date appointmentLastUpdatedDate) {this.appointmentLastUpdatedDate = appointmentLastUpdatedDate;}

    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public Date getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(Date confirmedDate) {
        this.confirmedDate = confirmedDate;
    }
}
