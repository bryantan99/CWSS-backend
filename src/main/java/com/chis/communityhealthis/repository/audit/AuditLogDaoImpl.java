package com.chis.communityhealthis.repository.audit;

import com.chis.communityhealthis.bean.AuditBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class AuditLogDaoImpl extends GenericDaoImpl<AuditBean, Integer> implements AuditLogDao {
}
