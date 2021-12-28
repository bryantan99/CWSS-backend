package com.chis.communityhealthis.model.user;

import java.util.Date;

public class BlockDetailModel {
    private String username;
    private Boolean isBlocked;
    private String blockedBy;
    private String blockedByFullName;
    private Date blockedDate;
    private String blockedMessage;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public String getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(String blockedBy) {
        this.blockedBy = blockedBy;
    }

    public String getBlockedByFullName() {
        return blockedByFullName;
    }

    public void setBlockedByFullName(String blockedByFullName) {
        this.blockedByFullName = blockedByFullName;
    }

    public Date getBlockedDate() {
        return blockedDate;
    }

    public void setBlockedDate(Date blockedDate) {
        this.blockedDate = blockedDate;
    }

    public String getBlockedMessage() {
        return blockedMessage;
    }

    public void setBlockedMessage(String blockedMessage) {
        this.blockedMessage = blockedMessage;
    }
}
