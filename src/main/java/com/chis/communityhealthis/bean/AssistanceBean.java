package com.chis.communityhealthis.bean;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.javers.core.metamodel.annotation.TypeName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ASSISTANCE")
@TypeName("AssistanceBean")
public class AssistanceBean implements Serializable {

    private static final long serialVersionUID = 2132065858891625782L;

    public static final String ASSISTANCE_ID = "ASSISTANCE_ID";
    public static final String USERNAME = "USERNAME";
    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String ASSISTANCE_TITLE = "ASSISTANCE_TITLE";
    public static final String ASSISTANCE_DESCRIPTION = "ASSISTANCE_DESCRIPTION";
    public static final String ASSISTANCE_STATUS = "ASSISTANCE_STATUS";
    public static final String CREATED_BY = "CREATED_BY";
    public static final String CREATED_DATE = "CREATED_DATE";
    public static final String ADMIN_USERNAME = "ADMIN_USERNAME";
    public static final String LAST_UPDATED_BY = "LAST_UPDATED_BY";
    public static final String LAST_UPDATED_DATE = "LAST_UPDATED_DATE";

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_ACCEPTED = "accepted";
    public static final String STATUS_REJECTED = "rejected";
    public static final String STATUS_PROCESSING = "processing";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_CANCELLED = "cancelled";

    public static final Integer CATEGORY_ID_NULL_CODE = -1;

    @Id
    @Column(name = ASSISTANCE_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assistanceId;
    private String username;

    @Column(name = CATEGORY_ID)
    private Integer categoryId;

    @Column(name = ASSISTANCE_TITLE)
    private String assistanceTitle;

    @Column(name = ASSISTANCE_DESCRIPTION)
    private String assistanceDescription;

    @Column(name = ASSISTANCE_STATUS)
    private String assistanceStatus;

    @Column(name = CREATED_BY)
    private String createdBy;

    @Column(name = CREATED_DATE)
    private Date createdDate;

    @Column(name = ADMIN_USERNAME)
    private String adminUsername;

    @Column(name = LAST_UPDATED_BY)
    private String lastUpdatedBy;

    @Column(name = LAST_UPDATED_DATE)
    private Date lastUpdatedDate;

    @OneToOne
    @JoinColumn(name = CATEGORY_ID, referencedColumnName = AssistanceCategoryBean.CATEGORY_ID, insertable = false, updatable = false)
    private AssistanceCategoryBean categoryBean;

    @OneToOne(mappedBy = "assistanceBean")
    private AppointmentBean appointmentBean;

    @OneToOne
    @JoinColumn(name = USERNAME, referencedColumnName = CommunityUserBean.USERNAME, insertable = false, updatable = false)
    @NotFound(action= NotFoundAction.IGNORE)
    private CommunityUserBean communityUserBean;

    @OneToOne
    @JoinColumn(name = ADMIN_USERNAME, referencedColumnName = AdminBean.USERNAME, insertable = false, updatable = false)
    @NotFound(action=NotFoundAction.IGNORE)
    private AdminBean adminBean;

    public Integer getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(Integer assistanceId) {
        this.assistanceId = assistanceId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAssistanceStatus() {
        return assistanceStatus;
    }

    public void setAssistanceStatus(String assistanceStatus) {
        this.assistanceStatus = assistanceStatus;
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

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public AppointmentBean getAppointmentBean() {
        return appointmentBean;
    }

    public void setAppointmentBean(AppointmentBean appointmentBean) {
        this.appointmentBean = appointmentBean;
    }

    public CommunityUserBean getCommunityUserBean() {
        return communityUserBean;
    }

    public void setCommunityUserBean(CommunityUserBean communityUserBean) {
        this.communityUserBean = communityUserBean;
    }

    public AdminBean getAdminBean() {
        return adminBean;
    }

    public void setAdminBean(AdminBean adminBean) {
        this.adminBean = adminBean;
    }

    public AssistanceCategoryBean getCategoryBean() {
        return categoryBean;
    }

    public void setCategoryBean(AssistanceCategoryBean categoryBean) {
        this.categoryBean = categoryBean;
    }
}

