package com.chis.communityhealthis.model.assistance;

import java.util.Date;

public class AssistanceUpdateForm {
    private Integer assistanceId;
    private Integer categoryId;
    private String title;
    private String description;
    private String status;
    private String personInCharge;
    private String updatedBy;
    private Date updatedDate;
    private Date appointmentStartDatetime;

    public Integer getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(Integer assistanceId) {
        this.assistanceId = assistanceId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
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

    public Date getAppointmentStartDatetime() {
        return appointmentStartDatetime;
    }

    public void setAppointmentStartDatetime(Date appointmentStartDatetime) {
        this.appointmentStartDatetime = appointmentStartDatetime;
    }
}
