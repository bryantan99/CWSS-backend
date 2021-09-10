package com.chis.communityhealthis.service.assistance;

import com.chis.communityhealthis.bean.AssistanceBean;
import com.chis.communityhealthis.model.assistance.AssistanceRecordTableModel;
import com.chis.communityhealthis.model.assistance.AssistanceRequestForm;

import java.util.List;

public interface AssistanceService {
    List<AssistanceRecordTableModel> getAssistanceRecords();
    List<AssistanceRecordTableModel> findUserAssistanceRecords(String username);
    AssistanceBean addAssistanceRequest(AssistanceRequestForm form);
    void deleteAssistance(Integer assistanceId, String actionMakerUsername) throws Exception;
    AssistanceBean getAssistanceRecordDetail(Integer assistanceId, String actionMakerUsername) throws Exception;
    List<AssistanceRecordTableModel> findAllAssistanceRecords();
}
