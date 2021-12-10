package com.chis.communityhealthis.service.audit;

import com.chis.communityhealthis.bean.AuditActionBean;
import com.chis.communityhealthis.bean.AuditBean;
import com.chis.communityhealthis.model.audit.AuditModel;

import java.util.Collection;
import java.util.List;

public interface AuditService {
    List<AuditModel> getAuditLogs(String moduleName);
    void saveLogs(AuditBean auditBean, Collection<AuditActionBean> auditActionBeans);
}
