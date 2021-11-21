package com.chis.communityhealthis.repository.assistance;

import com.chis.communityhealthis.bean.AssistanceBean;
import com.chis.communityhealthis.model.assistance.AssistanceQueryForm;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface AssistanceDao extends GenericDao<AssistanceBean, Integer> {
    List<AssistanceBean> findUserAssistanceRecords(AssistanceQueryForm form);
    List<AssistanceBean> findPendingAssistanceRecords(Integer assistanceId);
    List<AssistanceBean> findAdminHandledAssistanceRecords(AssistanceQueryForm queryForm);
    List<AssistanceBean> findAllAssistanceRecords(AssistanceQueryForm form);
}
