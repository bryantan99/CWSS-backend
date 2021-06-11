package com.chis.communityhealthis.service.assistance;

import com.chis.communityhealthis.bean.AssistanceBean;
import com.chis.communityhealthis.model.assistance.AssistanceRecordTableModel;
import com.chis.communityhealthis.repository.assistance.AssistanceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AssistanceServiceImpl implements AssistanceService{

    @Autowired
    private AssistanceDao assistanceDao;

    @Override
    public List<AssistanceRecordTableModel> getAssistanceRecords() {
        List<AssistanceBean> beans = assistanceDao.getAll();
        List<AssistanceRecordTableModel> list = new ArrayList<>();

        if (!CollectionUtils.isEmpty(beans)) {
            for (AssistanceBean bean: beans) {
                list.add(toAssistanceRecordTableModel(bean));
            }
        }

        return list;
    }

    private AssistanceRecordTableModel toAssistanceRecordTableModel(AssistanceBean bean) {
        AssistanceRecordTableModel model = new AssistanceRecordTableModel();
        model.setAssistanceId(bean.getAssistanceId());
        model.setApplicantUsername(bean.getUsername());
        model.setApplicantName(null);
        model.setIssueTitle(bean.getAssistanceTitle());
        model.setAdminName(null);
        model.setStatus(bean.getAssistanceStatus());
        return model;
    }

}
