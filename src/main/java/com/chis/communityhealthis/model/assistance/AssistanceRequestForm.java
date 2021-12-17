package com.chis.communityhealthis.model.assistance;

import java.util.Date;

public class AssistanceRequestForm {
    private Integer assistanceId;
    private Integer categoryId;
    private String assistanceTitle;
    private String assistanceDescription;
    private String createdBy;
    private Date createdDate;
    private String username;
    private String adminUsername;
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

    public String getAssistanceTitle() {
        return assistanceTitle;
    }

    public void setAssistanceTitle(String assistanceTitle) {
        this.assistanceTitle = assistanceTitle;
    }

    public String getAssistanceDescription() {
        return assistanceDescription;
    }

    public void setAssistanceDescription(String assistanceDescription) {
        this.assistanceDescription = assistanceDescription;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public Date getAppointmentStartDatetime() {
        return appointmentStartDatetime;
    }

    public void setAppointmentStartDatetime(Date appointmentStartDatetime) {
        this.appointmentStartDatetime = appointmentStartDatetime;
    }
}
