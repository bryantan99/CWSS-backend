package com.chis.communityhealthis.model.audit;

import java.util.Date;
import java.util.List;

public class AuditModel {
    private Integer auditId;
    private String module;
    private String actionBy;
    private String actionByFullName;
    private String actionName;
    private Date actionDate;
    private List<String> actionDescriptions;

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public String getActionByFullName() {
        return actionByFullName;
    }

    public void setActionByFullName(String actionByFullName) {
        this.actionByFullName = actionByFullName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public List<String> getActionDescriptions() {
        return actionDescriptions;
    }

    public void setActionDescriptions(List<String> actionDescriptions) {
        this.actionDescriptions = actionDescriptions;
    }
}
