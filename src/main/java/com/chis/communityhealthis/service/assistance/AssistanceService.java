package com.chis.communityhealthis.service.assistance;

import com.chis.communityhealthis.bean.AssistanceBean;
import com.chis.communityhealthis.model.assistance.*;
import com.chis.communityhealthis.model.assistancecategory.AssistanceCategoryForm;

import java.util.List;

public interface AssistanceService {
    List<AssistanceModel> findUserAssistanceRecords(AssistanceQueryForm form);
    AssistanceBean addAssistanceRequest(AssistanceRequestForm form);
    void deleteAssistance(Integer assistanceId, String actionMakerUsername) throws Exception;
    AssistanceModel getAssistanceRecordDetail(Integer assistanceId, String actionMakerUsername) throws Exception;
    List<AssistanceModel> findAllAssistanceRecords(AssistanceQueryForm form);
    void updateRecord(AssistanceUpdateForm form) throws Exception;
    List<AssistanceModel> getPendingAssistanceRecords(Integer assistanceId);
    List<AssistanceModel> getAdminHandledAssistanceRecords(AssistanceQueryForm queryForm);
    void acceptAssistanceRequest(AssistanceUpdateForm form) throws Exception;
    void rejectAssistanceForm(AssistanceRejectForm form) throws Exception;
    void deleteCategory(Integer categoryId) throws Exception;
    Integer addCategory(AssistanceCategoryForm form);
    void updateCategory(AssistanceCategoryForm form) throws Exception;
}
