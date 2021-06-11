package com.chis.communityhealthis.service.assistance;

import com.chis.communityhealthis.model.assistance.AssistanceRecordTableModel;

import java.util.List;

public interface AssistanceService {
    public List<AssistanceRecordTableModel> getAssistanceRecords();
}
