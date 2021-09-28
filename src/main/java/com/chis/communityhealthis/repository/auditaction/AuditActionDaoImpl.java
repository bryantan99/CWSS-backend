package com.chis.communityhealthis.repository.auditaction;

import com.chis.communityhealthis.bean.AuditActionBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class AuditActionDaoImpl extends GenericDaoImpl<AuditActionBean, Integer> implements AuditActionDao {
}
