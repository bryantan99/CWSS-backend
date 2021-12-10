package com.chis.communityhealthis.bean;

import org.javers.core.metamodel.annotation.TypeName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "HEALTH_ISSUE")
@TypeName("HealthIssueBean")
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

    @OneToOne
    @JoinColumn(name = DISEASE_ID, referencedColumnName = DiseaseBean.DISEASE_ID, updatable = false, insertable = false)
    private DiseaseBean diseaseBean;

    @OneToOne
    @JoinColumn(name = APPROVED_BY, referencedColumnName = AdminBean.USERNAME, updatable = false, insertable = false)
    private AdminBean adminBean;

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

    public DiseaseBean getDiseaseBean() { return diseaseBean; }

    public void setDiseaseBean(DiseaseBean diseaseBean) { this.diseaseBean = diseaseBean; }

    public AdminBean getAdminBean() { return adminBean; }

    public void setAdminBean(AdminBean adminBean) { this.adminBean = adminBean; }


}
