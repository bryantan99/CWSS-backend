package com.chis.communityhealthis.model.appointment;

import java.util.Date;

public class UpdateDatetimeForm {

    private Integer appointmentId;
    private Date appointmentLastUpdatedDate;
    private Date datetime;
    private String updatedBy;
    private Date updatedDate;
    private Integer assistanceId;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getAppointmentLastUpdatedDate() {
        return appointmentLastUpdatedDate;
    }

    public void setAppointmentLastUpdatedDate(Date appointmentLastUpdatedDate) {
        this.appointmentLastUpdatedDate = appointmentLastUpdatedDate;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(Integer assistanceId) {
        this.assistanceId = assistanceId;
    }
}
