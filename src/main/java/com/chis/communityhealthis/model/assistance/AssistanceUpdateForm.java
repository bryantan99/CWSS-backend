package com.chis.communityhealthis.model.assistance;

import java.util.Date;

public class AssistanceUpdateForm {
    private Integer assistanceId;
    private String status;
    private String personInCharge;
    private String updatedBy;
    private Date updatedDate;

    public Integer getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(Integer assistanceId) {
        this.assistanceId = assistanceId;
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
}
