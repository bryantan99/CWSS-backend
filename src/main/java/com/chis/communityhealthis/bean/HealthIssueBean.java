package com.chis.communityhealthis.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "HEALTH_ISSUE")
public class HealthIssueBean implements Serializable {

    private static final long serialVersionUID = -7952514692827576204L;

    public static final String ISSUE_ID = "ISSUE_ID";
    public static final String USERNAME = "USERNAME";
    public static final String DISEASE_ID = "DISEASE_ID";
    public static final String CREATED_DATE = "CREATED_DATE";
    public static final String CREATED_BY = "CREATED_BY";
    public static final String ISSUE_DESCRIPTION = "ISSUE_DESCRIPTION";
    public static final String APPROVED_BY = "APPROVED_BY";
    public static final String APPROVED_DATE = "APPROVED_DATE";

    @Id
    @Column(name = ISSUE_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer issueId;

    @Column(name = USERNAME)
    private String username;

    @Column(name = DISEASE_ID)
    private Integer diseaseId;

    @Column(name = CREATED_DATE)
    private Date createdDate;

    @Column(name = CREATED_BY)
    private String createdBy;

    @Column(name = ISSUE_DESCRIPTION)
    private String issueDescription;

    @Column(name = APPROVED_BY)
    private String approvedBy;

    @Column(name = APPROVED_DATE)
    private Date approvedDate;

    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId) {
        this.diseaseId = diseaseId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }
}
