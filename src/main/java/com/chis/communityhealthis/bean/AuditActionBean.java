package com.chis.communityhealthis.bean;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "audit_action")
public class AuditActionBean implements Serializable {

    private static final long serialVersionUID = 2413481799666337386L;

    public static final String AUDIT_ACTION_ID = "AUDIT_ACTION_ID";
    public static final String AUDIT_ID = "AUDIT_ID";
    public static final String ACTION_DESCRIPTION = "ACTION_DESCRIPTION";

    @Id
    @Column(name = AUDIT_ACTION_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auditActionId;

    @Column(name = AUDIT_ID)
    private Integer auditId;

    @Column(name = ACTION_DESCRIPTION)
    private String actionDescription;

    public AuditActionBean() {
    }

    public AuditActionBean(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public Integer getAuditActionId() {
        return auditActionId;
    }

    public void setAuditActionId(Integer id) {
        this.auditActionId = id;
    }

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditLogId) {
        this.auditId = auditLogId;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String description) {
        this.actionDescription = description;
    }
}
