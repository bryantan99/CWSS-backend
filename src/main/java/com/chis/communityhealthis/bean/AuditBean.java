package com.chis.communityhealthis.bean;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "audit")
public class AuditBean implements Serializable {

    private static final long serialVersionUID = -9191945426854562498L;

    public static final String AUDIT_ID = "AUDIT_ID";
    public static final String MODULE = "MODULE";
    public static final String ACTION_NAME = "ACTION_NAME";
    public static final String ACTION_BY = "ACTION_BY";
    public static final String ACTION_DATE = "ACTION_DATE";

    @Id
    @Column(name = AUDIT_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auditId;

    @Column(name = MODULE)
    private String module;

    @Column(name = ACTION_NAME)
    private String actionName;

    @Column(name = ACTION_BY)
    private String actionBy;

    @Column(name = ACTION_DATE)
    private Date actionDate;

    @OneToOne
    @NotFound(action= NotFoundAction.IGNORE)
    @JoinColumn(name = ACTION_BY, referencedColumnName = CommunityUserBean.USERNAME, updatable = false, insertable = false)
    private CommunityUserBean communityUserBean;

    @OneToOne
    @NotFound(action= NotFoundAction.IGNORE)
    @JoinColumn(name = ACTION_BY, referencedColumnName = AdminBean.USERNAME, updatable = false, insertable = false)
    private AdminBean adminBean;

    @OneToMany
    @JoinColumn(name = AUDIT_ID, referencedColumnName = AuditActionBean.AUDIT_ID, updatable = false, insertable = false)
    private Set<AuditActionBean> auditActionBeans;

    public AuditBean() {
    }

    public AuditBean(String module, String actionName, String actionBy) {
        this.module = module;
        this.actionName = actionName;
        this.actionBy = actionBy;
        this.actionDate = new Date();
    }

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditLogId) {
        this.auditId = auditLogId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
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

    public Set<AuditActionBean> getAuditActionBeans() {
        return auditActionBeans;
    }

    public void setAuditActionBeans(Set<AuditActionBean> auditActionBeans) {
        this.auditActionBeans = auditActionBeans;
    }
}
