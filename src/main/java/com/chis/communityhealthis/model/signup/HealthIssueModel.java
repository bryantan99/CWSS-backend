package com.chis.communityhealthis.model.signup;

public class HealthIssueModel {
    private Integer healthIssueId;
    private Integer diseaseId;
    private String description;
    private Boolean isApproved;

    public Integer getHealthIssueId() { return healthIssueId; }

    public void setHealthIssueId(Integer healthIssueId) { this.healthIssueId = healthIssueId; }

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean approved) {
        isApproved = approved;
    }
}
