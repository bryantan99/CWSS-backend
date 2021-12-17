package com.chis.communityhealthis.model.appointment;

import java.util.Date;

public class UpdateAppointmentStatusForm {
    private Integer appointmentId;
    private String appointmentStatus;
    private Integer assistanceId;
    private String assistanceStatus;
    private String reason;
    private String submittedBy;
    private Date submittedDate;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Integer getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(Integer assistanceId) {
        this.assistanceId = assistanceId;
    }

    public String getAssistanceStatus() {
        return assistanceStatus;
    }

    public void setAssistanceStatus(String assistanceStatus) {
        this.assistanceStatus = assistanceStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }
}
