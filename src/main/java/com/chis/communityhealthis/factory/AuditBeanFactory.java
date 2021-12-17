package com.chis.communityhealthis.factory;

import com.chis.communityhealthis.bean.AuditBean;

import java.util.Date;

public class AuditBeanFactory {
    private final String module;
    private final String actionName;
    private final String actionMaker;

    public AuditBeanFactory(String module, String actionName, String actionMaker) {
        this.module = module;
        this.actionName = actionName;
        this.actionMaker = actionMaker;
    }

    public AuditBean createAuditBean() {
        AuditBean bean = new AuditBean();
        bean.setModule(this.module);
        bean.setActionName(this.actionName);
        bean.setActionBy(this.actionMaker);
        bean.setActionDate(new Date());
        return bean;
    }
}
