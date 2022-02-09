package com.chis.communityhealthis.service.assistance;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.factory.AppointmentModelFactory;
import com.chis.communityhealthis.model.appointment.AppointmentModel;
import com.chis.communityhealthis.model.assistance.*;
import com.chis.communityhealthis.model.assistancecategory.AssistanceCategoryForm;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.repository.appointment.AppointmentDao;
import com.chis.communityhealthis.repository.assistance.AssistanceDao;
import com.chis.communityhealthis.repository.assistancecategory.AssistanceCategoryDao;
import com.chis.communityhealthis.repository.assistancecomment.AssistanceCommentDao;
import com.chis.communityhealthis.service.audit.AuditService;
import com.chis.communityhealthis.utility.AuditConstant;
import com.chis.communityhealthis.utility.BeanComparator;
import com.chis.communityhealthis.utility.DatetimeUtil;
import io.jsonwebtoken.lang.Assert;
import javassist.NotFoundException;
import org.apache.commons.lang3.SerializationUtils;
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

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private AuditService auditService;

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
    public AssistanceBean addAssistanceRequest(AssistanceRequestForm form) throws Exception {
        AssistanceBean bean = createAssistanceBean(form);
        Integer assistanceId = assistanceDao.add(bean);
        if (assistanceId == null) {
            throw new Exception("There's an error when saving assistance bean.");
        }
        form.setAssistanceId(assistanceId);
        AppointmentBean appointmentBean = createAppointmentBean(form);
        appointmentDao.add(appointmentBean);

        AuditBean auditBean = new AuditBean(AuditConstant.MODULE_ASSISTANCE, AuditConstant.formatActionCreateAssistanceRequest(assistanceId), form.getCreatedBy());
        auditService.saveLogs(auditBean, null);
        return bean;
    }

    @Override
    public AssistanceBean addAssistanceRequestByAdmin(AssistanceRequestForm form) throws Exception {
        AssistanceBean bean = new AssistanceBean();
        bean.setCategoryId(form.getCategoryId());
        bean.setAssistanceTitle(form.getAssistanceTitle());
        bean.setAssistanceDescription(form.getAssistanceDescription());
        bean.setAssistanceStatus(AssistanceBean.STATUS_ACCEPTED);
        bean.setUsername(form.getUsername());
        bean.setAdminUsername(form.getCreatedBy());
        bean.setCreatedDate(new Date());
        bean.setCreatedBy(form.getAdminUsername());

        Integer assistanceId = assistanceDao.add(bean);
        if (assistanceId == null) {
            throw new Exception("There's an error when saving assistance bean.");
        }

        if (form.getAppointmentStartDatetime() != null) {
            AppointmentBean appointmentBean = createAppointmentBean(form);
            appointmentDao.add(appointmentBean);

        }

        AuditBean auditBean = new AuditBean(AuditConstant.MODULE_ASSISTANCE, AuditConstant.formatActionCreateAssistanceRequest(assistanceId), form.getCreatedBy());
        auditService.saveLogs(auditBean, null);
        return bean;
    }

    @Override
    public void deleteAssistance(Integer assistanceId, String actionMakerUsername) throws Exception {
        AssistanceBean assistanceBean = assistanceDao.find(assistanceId);
        AdminBean adminBean = adminDao.find(actionMakerUsername);

        if (assistanceBean == null) {
            throw new Exception("Assistance bean [ID: " + assistanceId + "] was not found.");
        } else if (!StringUtils.equals(assistanceBean.getUsername(), actionMakerUsername) && adminBean == null) {
            throw new Exception("Unauthorized to delete assistance! Action maker is not admin / assistance request applicant.");
        }

        AppointmentBean appointmentBean = assistanceBean.getAppointmentBean();
        if (appointmentBean != null) {
            appointmentDao.remove(appointmentBean);
            AuditBean appointmentAuditBean = new AuditBean(AuditConstant.MODULE_APPOINTMENT, AuditConstant.formatActionDeleteAppointment(appointmentBean.getAppointmentId()), actionMakerUsername);
            auditService.saveLogs(appointmentAuditBean, null);
        }
        assistanceDao.remove(assistanceBean);
        AuditBean assistanceAuditBean = new AuditBean(AuditConstant.MODULE_ASSISTANCE, AuditConstant.formatActionDeleteAssistanceRequest(assistanceId), actionMakerUsername);
        auditService.saveLogs(assistanceAuditBean, null);
    }

    @Override
    public AssistanceModel getAssistanceRecordDetail(Integer assistanceId, String actionMakerUsername) throws Exception {
        AssistanceBean assistanceBean = assistanceDao.find(assistanceId);
        if (assistanceBean == null) {
            throw new NotFoundException("Assistance [ID: " + assistanceId.toString() + "] was not found.");
        }

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
        AssistanceBean clonedAssistanceBean = SerializationUtils.clone(assistanceBean);

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
        BeanComparator beanComparator = new BeanComparator(createPureAssistanceBean(clonedAssistanceBean), createPureAssistanceBean(assistanceBean));

        AuditBean auditBean = new AuditBean(AuditConstant.MODULE_ASSISTANCE, AuditConstant.formatActionUpdateAssistanceRequest(form.getAssistanceId()), form.getUpdatedBy());
        List<AuditActionBean> auditActionBeans = new ArrayList<>();
        if (beanComparator.hasChanges()) {
            auditActionBeans.add(new AuditActionBean(beanComparator.toPrettyString()));
        }
        auditService.saveLogs(auditBean, auditActionBeans);
    }

    private AssistanceBean createPureAssistanceBean(AssistanceBean assistanceBean) {
        AssistanceBean bean = new AssistanceBean();
        bean.setAssistanceId(assistanceBean.getAssistanceId());
        bean.setUsername(assistanceBean.getUsername());
        bean.setCategoryId(assistanceBean.getCategoryId());
        bean.setAssistanceTitle(assistanceBean.getAssistanceTitle());
        bean.setAssistanceDescription(assistanceBean.getAssistanceDescription());
        bean.setAssistanceStatus(assistanceBean.getAssistanceStatus());
        bean.setCreatedBy(assistanceBean.getCreatedBy());
        bean.setCreatedDate(assistanceBean.getCreatedDate());
        bean.setLastUpdatedBy(assistanceBean.getLastUpdatedBy());
        bean.setLastUpdatedDate(assistanceBean.getLastUpdatedDate());
        bean.setAdminUsername(assistanceBean.getAdminUsername());
        return bean;
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
    public void rejectAssistanceForm(AssistanceRejectForm form) throws Exception {
        AssistanceBean assistanceBean = assistanceDao.find(form.getAssistanceId());
        if (assistanceBean == null) {
            throw new Exception("Assistance [ID: " + form.getAssistanceId() + "] was not found.");
        }
        assistanceBean.setAssistanceStatus(AssistanceBean.STATUS_REJECTED);
        assistanceBean.setLastUpdatedBy(form.getRejectedBy());
        assistanceBean.setLastUpdatedDate(form.getRejectedDate());
        assistanceDao.update(assistanceBean);

        AppointmentBean appointmentBean = assistanceBean.getAppointmentBean();
        appointmentBean.setAppointmentStatus(AppointmentBean.APPOINTMENT_STATUS_CANCELLED);
        appointmentBean.setLastUpdatedDate(form.getRejectedDate());
        appointmentBean.setLastUpdatedBy(form.getRejectedBy());
        appointmentDao.update(appointmentBean);

        AssistanceCommentBean commentBean = new AssistanceCommentBean();
        commentBean.setAssistanceId(form.getAssistanceId());
        commentBean.setCommentDesc("I've rejected the request. (Reason : " + form.getReason() + ")");
        commentBean.setCreatedBy(form.getRejectedBy());
        commentBean.setCreatedDate(form.getRejectedDate());
        assistanceCommentDao.add(commentBean);
    }

    @Override
    public void deleteCategory(Integer categoryId, String actionMakerUsername) throws Exception {
        AssistanceCategoryBean bean = categoryDao.find(categoryId);
        if (bean == null) {
            throw new Exception("Bean [ID: " + categoryId + "] was not found.");
        }
        categoryDao.remove(bean);
        AuditBean auditBean = new AuditBean(AuditConstant.MODULE_ASSISTANCE, AuditConstant.formatActionDeleteAssistanceCategory(bean.getCategoryName()), actionMakerUsername);
        auditService.saveLogs(auditBean, null);
    }

    @Override
    public Integer addCategory(AssistanceCategoryForm form) {
        AssistanceCategoryBean bean = new AssistanceCategoryBean();
        bean.setCategoryName(form.getCategoryName());
        AuditBean auditBean = new AuditBean(AuditConstant.MODULE_ASSISTANCE, AuditConstant.formatActionCreateAssistanceCategory(bean.getCategoryName()), form.getActionBy());
        auditService.saveLogs(auditBean, null);
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
        AuditBean auditBean = new AuditBean(AuditConstant.MODULE_ASSISTANCE, AuditConstant.formatActionUpdateAssistanceCategory(bean.getCategoryName()), form.getActionBy());
        auditService.saveLogs(auditBean, null);
    }

    private AssistanceBean createAssistanceBean(AssistanceRequestForm form) {
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
        return bean;
    }

    private AppointmentBean createAppointmentBean(AssistanceRequestForm form) {
        boolean isAdmin = adminDao.find(form.getCreatedBy()) != null;
        String status = isAdmin ? AppointmentBean.APPOINTMENT_STATUS_PENDING_USER : AppointmentBean.APPOINTMENT_STATUS_PENDING_ADMIN;

        AppointmentBean appointmentBean = new AppointmentBean();
        appointmentBean.setAppointmentPurpose("Discuss about assistance request - " + form.getAssistanceTitle());
        appointmentBean.setAppointmentStartTime(form.getAppointmentStartDatetime());
        appointmentBean.setAppointmentEndTime(DatetimeUtil.calculateAppointmentEndDatetime(form.getAppointmentStartDatetime()));
        appointmentBean.setAppointmentStatus(status);
        appointmentBean.setCreatedBy(form.getCreatedBy());
        appointmentBean.setCreatedDate(new Date());
        appointmentBean.setAdminUsername(StringUtils.isNotBlank(form.getAdminUsername()) ? form.getAdminUsername() : null);
        appointmentBean.setUsername(isAdmin ? form.getUsername() : form.getCreatedBy());
        appointmentBean.setAssistanceId(form.getAssistanceId());
        return appointmentBean;
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

        AppointmentBean appointmentBean = bean.getAppointmentBean();
        if (appointmentBean != null) {
            AppointmentModel appointmentModel = AppointmentModelFactory.createAppointmentModel(appointmentBean);
            model.setAppointmentModel(appointmentModel);
        }

        return model;
    }
}
