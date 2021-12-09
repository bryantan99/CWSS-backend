package com.chis.communityhealthis.repository.audit;

import com.chis.communityhealthis.bean.AuditBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface AuditLogDao extends GenericDao<AuditBean,Integer> {
    List<AuditBean> getAuditBeans(String moduleName);
}
