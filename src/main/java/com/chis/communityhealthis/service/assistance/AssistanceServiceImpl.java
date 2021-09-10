package com.chis.communityhealthis.service.assistance;

import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.bean.AssistanceBean;
import com.chis.communityhealthis.model.assistance.AssistanceRecordTableModel;
import com.chis.communityhealthis.model.assistance.AssistanceRequestForm;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.repository.assistance.AssistanceDao;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AssistanceServiceImpl implements AssistanceService{

    @Autowired
    private AssistanceDao assistanceDao;

    @Autowired
    private AdminDao  adminDao;

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

    @Override
    public List<AssistanceRecordTableModel> findUserAssistanceRecords(String username) {
        List<AssistanceRecordTableModel> list = new ArrayList<>();
        List<AssistanceBean> assistanceBeans = assistanceDao.findUserAssistanceRecords(username);

        if (!CollectionUtils.isEmpty(assistanceBeans)) {
            for (AssistanceBean assistanceBean: assistanceBeans) {
                list.add(toAssistanceRecordTableModel(assistanceBean));
            }
        }

        return list;
    }

    @Override
    public AssistanceBean addAssistanceRequest(AssistanceRequestForm form) {
        AssistanceBean bean = new AssistanceBean();
        bean.setCategoryId(form.getCategoryId());
        bean.setAssistanceTitle(form.getAssistanceTitle());
        bean.setAssistanceDescription(form.getAssistanceDescription());
        bean.setCreatedDate(new Date());
        bean.setCreatedBy(form.getCreatedBy());
        bean.setUsername(form.getUsername());
        bean.setAdminUsername(form.getAdminUsername());
        bean.setAssistanceStatus("pending");
        assistanceDao.add(bean);
        return bean;
    }

    @Override
    public void deleteAssistance(Integer assistanceId, String actionMakerUsername) throws Exception {
        AssistanceBean assistanceBean = assistanceDao.find(assistanceId);
        Assert.notNull(assistanceBean, "Assistance bean with Id: " + assistanceId.toString() + " was not found!");
        Assert.isTrue(assistanceBean.getAssistanceStatus().equals("pending"), "Assistance request's status is not in pending.");

        AdminBean adminBean = adminDao.find(actionMakerUsername);
        if (assistanceBean.getUsername().equals(actionMakerUsername) || adminBean != null) {
            assistanceDao.remove(assistanceBean);
        } else {
            throw new Exception("Unauthorized to delete assistance! Action maker is not admin / assistance request applicant.");
        }
    }

    private AssistanceRecordTableModel toAssistanceRecordTableModel(AssistanceBean bean) {
        AssistanceRecordTableModel model = new AssistanceRecordTableModel();
        model.setAssistanceId(bean.getAssistanceId());
        model.setUsername(bean.getUsername());
        model.setUserFullName(null);
        model.setAssistanceTitle(bean.getAssistanceTitle());
        model.setAdminFullName(null);
        model.setStatus(bean.getAssistanceStatus());
        model.setCreatedDate(bean.getCreatedDate());
        return model;
    }
}
