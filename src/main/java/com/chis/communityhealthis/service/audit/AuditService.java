package com.chis.communityhealthis.service.audit;

import com.chis.communityhealthis.model.audit.AuditModel;

import java.util.List;

public interface AuditService {
    List<AuditModel> getAuditLogs(String moduleName);
}
