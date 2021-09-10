package com.chis.communityhealthis.model.assistance;

import java.util.Date;

public class AssistanceRecordTableModel {
    private Integer assistanceId;
    private String assistanceTitle;
    private String username;
    private String userFullName;
    private String adminUsername;
    private String adminFullName;
    private String status;
    private Date createdDate;

    public Integer getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(Integer assistanceId) {
        this.assistanceId = assistanceId;
    }

    public String getAssistanceTitle() {
        return assistanceTitle;
    }

    public void setAssistanceTitle(String assistanceTitle) {
        this.assistanceTitle = assistanceTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminFullName() {
        return adminFullName;
    }

    public void setAdminFullName(String adminFullName) {
        this.adminFullName = adminFullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
