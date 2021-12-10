package com.chis.communityhealthis.service.audit;

import com.chis.communityhealthis.bean.AuditActionBean;
import com.chis.communityhealthis.bean.AuditBean;
import com.chis.communityhealthis.model.audit.AuditModel;
import com.chis.communityhealthis.repository.audit.AuditLogDao;
import com.chis.communityhealthis.repository.auditaction.AuditActionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class AuditServiceImpl implements AuditService{

    @Autowired
    private AuditLogDao auditLogDao;

    @Autowired
    private AuditActionDao auditActionDao;

    @Override
    public List<AuditModel> getAuditLogs(String moduleName) {
        List<AuditModel> list = new ArrayList<>();
        List<AuditBean> auditBeans = auditLogDao.getAuditBeans(moduleName);
        if (!CollectionUtils.isEmpty(auditBeans)) {
            for (AuditBean auditBean : auditBeans) {
                list.add(toAuditModel(auditBean));
            }
        }
        return list;
    }

    @Override
    public void saveLogs(AuditBean auditBean, Collection<AuditActionBean> auditActionBeans) {
        Integer id = auditLogDao.add(auditBean);
        if (!CollectionUtils.isEmpty(auditActionBeans)) {
            for (AuditActionBean auditActionBean: auditActionBeans) {
                auditActionBean.setAuditId(id);
                auditActionDao.add(auditActionBean);
            }
        }
    }

    private AuditModel toAuditModel(AuditBean auditBean) {
        AuditModel auditModel = new AuditModel();
        auditModel.setAuditId(auditBean.getAuditId());
        auditModel.setModule(auditBean.getModule());
        auditModel.setActionBy(auditBean.getActionBy());
        auditModel.setActionByFullName(null);
        auditModel.setActionName(auditBean.getActionName());
        auditModel.setActionDate(auditBean.getActionDate());
        if (!CollectionUtils.isEmpty(auditBean.getAuditActionBeans())) {
            List<String> descriptionList = new ArrayList<>();
            for (AuditActionBean auditActionBean : auditBean.getAuditActionBeans()) {
                String desc = auditActionBean.getActionDescription();
                String formattedDesc = desc.replaceAll("(\r\n|\n)", "<br />");
                descriptionList.add(formattedDesc);
            }
            auditModel.setActionDescriptions(descriptionList);
        }
        return auditModel;
    }
}
