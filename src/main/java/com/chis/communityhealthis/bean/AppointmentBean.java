package com.chis.communityhealthis.bean;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.javers.core.metamodel.annotation.TypeName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "APPOINTMENT")
@TypeName("AppointmentBean")
public class AppointmentBean implements Serializable {

    private static final long serialVersionUID = 262380162656168802L;

    public static final String APPOINTMENT_ID = "APPOINTMENT_ID";
    public static final String APPOINTMENT_PURPOSE = "APPOINTMENT_PURPOSE";
    public static final String APPOINTMENT_START_TIME = "APPOINTMENT_START_TIME";
    public static final String APPOINTMENT_END_TIME = "APPOINTMENT_END_TIME";
    public static final String APPOINTMENT_STATUS = "APPOINTMENT_STATUS";
    public static final String CREATED_BY = "CREATED_BY";
    public static final String CREATED_DATE = "CREATED_DATE";
    public static final String ADMIN_USERNAME = "ADMIN_USERNAME";
    public static final String USERNAME = "USERNAME";
    public static final String LAST_UPDATED_BY = "LAST_UPDATED_BY";
    public static final String LAST_UPDATED_DATE = "LAST_UPDATED_DATE";
    public static final String ASSISTANCE_ID = "ASSISTANCE_ID";

    public static final String APPOINTMENT_STATUS_PENDING_USER = "pending_user";
    public static final String APPOINTMENT_STATUS_PENDING_ADMIN = "pending_admin";
    public static final String APPOINTMENT_STATUS_CANCELLED = "cancelled";
    public static final String APPOINTMENT_STATUS_CONFIRMED = "confirmed";
    public static final String APPOINTMENT_STATUS_COMPLETED = "completed";

    @Id
    @Column(name = APPOINTMENT_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    @Column(name = APPOINTMENT_PURPOSE)
    private String appointmentPurpose;

    @Column(name = APPOINTMENT_START_TIME)
    private Date appointmentStartTime;

    @Column(name = APPOINTMENT_END_TIME)
    private Date appointmentEndTime;

    @Column(name = APPOINTMENT_STATUS)
    private String appointmentStatus;

    @Column(name = CREATED_BY)
    private String createdBy;

    @Column(name = CREATED_DATE)
    private Date createdDate;

    @Column(name = ADMIN_USERNAME)
    private String adminUsername;

    @Column(name = USERNAME)
    private String username;

    @Column(name = LAST_UPDATED_BY)
    private String lastUpdatedBy;

    @Column(name = LAST_UPDATED_DATE)
    private Date lastUpdatedDate;

    @Column(name = ASSISTANCE_ID)
    private Integer assistanceId;

    @OneToOne()
    @JoinColumn(name = ASSISTANCE_ID, referencedColumnName = AssistanceBean.ASSISTANCE_ID, insertable = false, updatable = false)
    private AssistanceBean assistanceBean;

    @OneToOne
    @JoinColumn(name = ADMIN_USERNAME, referencedColumnName = AdminBean.USERNAME, insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private AdminBean adminBean;

    @OneToOne
    @JoinColumn(name = USERNAME, referencedColumnName = CommunityUserBean.USERNAME, insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private CommunityUserBean communityUserBean;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentPurpose() {
        return appointmentPurpose;
    }

    public void setAppointmentPurpose(String appointmentPurpose) {
        this.appointmentPurpose = appointmentPurpose;
    }

    public Date getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(Date appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public Date getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(Date appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Integer getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(Integer assistanceId) {
        this.assistanceId = assistanceId;
    }

    public AdminBean getAdminBean() {
        return adminBean;
    }

    public void setAdminBean(AdminBean adminBean) {
        this.adminBean = adminBean;
    }

    public CommunityUserBean getCommunityUserBean() {
        return communityUserBean;
    }

    public void setCommunityUserBean(CommunityUserBean communityUserBean) {
        this.communityUserBean = communityUserBean;
    }

    public AssistanceBean getAssistanceBean() {
        return assistanceBean;
    }

    public void setAssistanceBean(AssistanceBean assistanceBean) {
        this.assistanceBean = assistanceBean;
    }
}
