package com.chis.communityhealthis.service.assistance;

import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.bean.AssistanceBean;
import com.chis.communityhealthis.bean.AssistanceCategoryBean;
import com.chis.communityhealthis.bean.AssistanceCommentBean;
import com.chis.communityhealthis.model.assistance.*;
import com.chis.communityhealthis.model.assistancecategory.AssistanceCategoryForm;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.repository.assistance.AssistanceDao;
import com.chis.communityhealthis.repository.assistancecategory.AssistanceCategoryDao;
import com.chis.communityhealthis.repository.assistancecomment.AssistanceCommentDao;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AssistanceServiceImpl implements AssistanceService {

    @Autowired
    private AssistanceDao assistanceDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private AssistanceCategoryDao categoryDao;

    @Autowired
    private AssistanceCommentDao assistanceCommentDao;

    @Override
    public List<AssistanceModel> findUserAssistanceRecords(AssistanceQueryForm form) {
        List<AssistanceModel> list = new ArrayList<>();
        List<AssistanceBean> assistanceBeans = assistanceDao.findUserAssistanceRecords(form);
        if (!CollectionUtils.isEmpty(assistanceBeans)) {
            for (AssistanceBean assistanceBean : assistanceBeans) {
                list.add(toAssistanceModel(assistanceBean));
            }
        }
        return list;
    }

    @Override
    public AssistanceBean addAssistanceRequest(AssistanceRequestForm form) {
        AssistanceBean bean = new AssistanceBean();
        bean.setAssistanceTitle(form.getAssistanceTitle());
        bean.setAssistanceDescription(form.getAssistanceDescription());
        bean.setCreatedDate(new Date());
        bean.setCreatedBy(form.getCreatedBy());
        bean.setUsername(form.getUsername());

        if (form.getCategoryId() != null) {
            bean.setCategoryId(form.getCategoryId());
        }

        if (StringUtils.isNotBlank(form.getAdminUsername())) {
            bean.setAdminUsername(form.getAdminUsername());
            bean.setAssistanceStatus(AssistanceBean.STATUS_PROCESSING);
        } else {
            bean.setAssistanceStatus(AssistanceBean.STATUS_PENDING);
        }
        assistanceDao.add(bean);
        return bean;
    }

    @Override
    public void deleteAssistance(Integer assistanceId, String actionMakerUsername) throws Exception {
        AssistanceBean assistanceBean = assistanceDao.find(assistanceId);
        Assert.notNull(assistanceBean, "Assistance bean with Id: " + assistanceId.toString() + " was not found!");
        Assert.isTrue(assistanceBean.getAssistanceStatus().equals(AssistanceBean.STATUS_PENDING), "Assistance request's status is not in pending.");

        AdminBean adminBean = adminDao.find(actionMakerUsername);
        if (assistanceBean.getUsername().equals(actionMakerUsername) || adminBean != null) {
            assistanceDao.remove(assistanceBean);
        } else {
            throw new Exception("Unauthorized to delete assistance! Action maker is not admin / assistance request applicant.");
        }
    }

    @Override
    public AssistanceModel getAssistanceRecordDetail(Integer assistanceId, String actionMakerUsername) throws Exception {
        AssistanceBean assistanceBean = assistanceDao.find(assistanceId);
        Assert.notNull(assistanceBean, "Assistance bean with Id: " + assistanceId.toString() + " was not found!");

        AdminBean adminBean = adminDao.find(actionMakerUsername);
        if (adminBean == null && !assistanceBean.getUsername().equals(actionMakerUsername)) {
            throw new Exception("Unauthorized to retrieve assistance detail! Action maker is not admin / assistance request applicant.");
        }

        return toAssistanceModel(assistanceBean);
    }

    @Override
    public List<AssistanceModel> findAllAssistanceRecords(AssistanceQueryForm form) {
        List<AssistanceBean> assistanceBeans = assistanceDao.findAllAssistanceRecords(form);
        List<AssistanceModel> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assistanceBeans)) {
            for (AssistanceBean bean : assistanceBeans) {
                list.add(toAssistanceModel(bean));
            }
        }
        return list;
    }

    @Override
    public void updateRecord(AssistanceUpdateForm form) throws Exception {
        AssistanceBean assistanceBean = assistanceDao.find(form.getAssistanceId());
        Assert.notNull(assistanceBean, "Assistance Bean [ID: " + form.getAssistanceId().toString() + "] was not found!");

        boolean isAdmin = adminDao.find(form.getUpdatedBy()) != null;

        if (!isAdmin && !StringUtils.equals(assistanceBean.getAssistanceStatus(), AssistanceBean.STATUS_PENDING)) {
            throw new Exception("Assistance request is in " + assistanceBean.getAssistanceStatus() + " mode and cannot further edit by community user.");
        }

        if (isAdmin) {
            assistanceBean.setAssistanceStatus(form.getStatus());
            assistanceBean.setAdminUsername(form.getPersonInCharge());
            assistanceBean.setCategoryId(form.getCategoryId());
        } else {
            assistanceBean.setAssistanceTitle(form.getTitle());
            assistanceBean.setAssistanceDescription(form.getDescription());
        }

        assistanceBean.setLastUpdatedBy(form.getUpdatedBy());
        assistanceBean.setLastUpdatedDate(form.getUpdatedDate());
        assistanceDao.update(assistanceBean);
    }

    @Override
    public List<AssistanceModel> getPendingAssistanceRecords(Integer assistanceId) {
        List<AssistanceModel> list = new ArrayList<>();
        List<AssistanceBean> assistanceBeans = assistanceDao.findPendingAssistanceRecords(assistanceId);
        if (!CollectionUtils.isEmpty(assistanceBeans)) {
            for (AssistanceBean assistanceBean : assistanceBeans) {
                list.add(toAssistanceModel(assistanceBean));
            }
        }
        return list;
    }

    @Override
    public List<AssistanceModel> getAdminHandledAssistanceRecords(AssistanceQueryForm queryForm) {
        List<AssistanceModel> list = new ArrayList<>();
        List<AssistanceBean> assistanceBeans = assistanceDao.findAdminHandledAssistanceRecords(queryForm);
        if (!CollectionUtils.isEmpty(assistanceBeans)) {
            for (AssistanceBean bean : assistanceBeans) {
                list.add(toAssistanceModel(bean));
            }
        }
        return list;
    }

    @Override
    public void acceptAssistanceRequest(AssistanceUpdateForm form) throws Exception {
        AssistanceBean assistanceBean = assistanceDao.find(form.getAssistanceId());
        if (assistanceBean == null) {
            throw new Exception("Assistance [ID: " + form.getAssistanceId() + "] was not found.");
        } else if (!StringUtils.equals(AssistanceBean.STATUS_PENDING, assistanceBean.getAssistanceStatus())) {
            throw new Exception("Assistance [ID: " + form.getAssistanceId() + "] was not in pending status");
        } else if (StringUtils.isNotBlank(assistanceBean.getAdminUsername())) {
            throw new Exception("Assistance [ID: " + form.getAssistanceId() + "] was being handled by another person.");
        }
        assistanceBean.setAssistanceStatus(AssistanceBean.STATUS_PROCESSING);
        assistanceBean.setAdminUsername(form.getPersonInCharge());
        assistanceBean.setLastUpdatedBy(form.getUpdatedBy());
        assistanceBean.setLastUpdatedDate(form.getUpdatedDate());
        assistanceDao.update(assistanceBean);
    }

    @Override
    public void rejectAssistanceForm(AssistanceRejectForm form) throws Exception {
        AssistanceBean assistanceBean = assistanceDao.find(form.getAssistanceId());
        if (assistanceBean == null) {
            throw new Exception("Assistance [ID: " + form.getAssistanceId() + "] was not found.");
        }
        assistanceBean.setAssistanceStatus(AssistanceBean.STATUS_REJECTED);
        assistanceBean.setLastUpdatedBy(form.getRejectedBy());
        assistanceBean.setLastUpdatedDate(form.getRejectedDate());
        assistanceDao.update(assistanceBean);

        AssistanceCommentBean commentBean = new AssistanceCommentBean();
        commentBean.setAssistanceId(form.getAssistanceId());
        commentBean.setCommentDesc("Rejected Reason : " + form.getReason());
        commentBean.setCreatedBy(form.getRejectedBy());
        commentBean.setCreatedDate(form.getRejectedDate());
        assistanceCommentDao.add(commentBean);
    }

    @Override
    public void deleteCategory(Integer categoryId) throws Exception {
        AssistanceCategoryBean bean = categoryDao.find(categoryId);
        if (bean == null) {
            throw new Exception("Bean [ID: " + categoryId + "] was not found.");
        }
        categoryDao.remove(bean);
    }

    @Override
    public Integer addCategory(AssistanceCategoryForm form) {
        AssistanceCategoryBean bean = new AssistanceCategoryBean();
        bean.setCategoryName(form.getCategoryName());
        return categoryDao.add(bean);
    }

    @Override
    public void updateCategory(AssistanceCategoryForm form) throws Exception {
        AssistanceCategoryBean bean = categoryDao.find(form.getCategoryId());
        if (bean == null) {
            throw new Exception("Bean [ID: " + form.getCategoryId() + "] was not found.");
        }
        bean.setCategoryName(form.getCategoryName());
        categoryDao.update(bean);
    }

    private AssistanceRecordTableModel toAssistanceRecordTableModel(AssistanceBean bean) {
        AssistanceRecordTableModel model = new AssistanceRecordTableModel();
        model.setAssistanceId(bean.getAssistanceId());
        model.setUsername(bean.getUsername());
        model.setAssistanceTitle(bean.getAssistanceTitle());
        model.setStatus(bean.getAssistanceStatus());
        model.setCreatedDate(bean.getCreatedDate());

        if (bean.getCommunityUserBean() != null) {
            model.setUserFullName(bean.getCommunityUserBean().getFullName());
        }

        if (bean.getAdminBean() != null) {
            model.setAdminFullName(bean.getAdminBean().getFullName());
        }
        return model;
    }

    private AssistanceModel toAssistanceModel(AssistanceBean bean) {
        AssistanceModel model = new AssistanceModel();
        model.setAssistanceId(bean.getAssistanceId());
        model.setTitle(bean.getAssistanceTitle());
        model.setDescription(bean.getAssistanceDescription());
        model.setStatus(bean.getAssistanceStatus());
        model.setCreatedBy(bean.getCreatedBy());
        model.setCreatedDate(bean.getCreatedDate());
        model.setLastUpdatedBy(bean.getLastUpdatedBy());
        model.setLastUpdatedDate(bean.getLastUpdatedDate());
        if (bean.getCategoryBean() != null) {
            model.setCategoryId(bean.getCategoryId());
            model.setCategoryName(bean.getCategoryBean().getCategoryName());
        }
        if (bean.getCommunityUserBean() != null) {
            model.setUsername(bean.getUsername());
            model.setUserFullName(bean.getCommunityUserBean().getFullName());
        }
        if (bean.getAdminBean() != null) {
            model.setAdminUsername(bean.getAdminUsername());
            model.setAdminFullName(bean.getAdminBean().getFullName());
        }
        return model;
    }
}
