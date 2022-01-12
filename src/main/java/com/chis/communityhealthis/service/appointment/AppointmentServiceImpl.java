package com.chis.communityhealthis.service.appointment;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.factory.AppointmentModelFactory;
import com.chis.communityhealthis.model.appointment.AppointmentModel;
import com.chis.communityhealthis.model.appointment.ConfirmationForm;
import com.chis.communityhealthis.model.appointment.UpdateAppointmentStatusForm;
import com.chis.communityhealthis.model.appointment.UpdateDatetimeForm;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.repository.appointment.AppointmentDao;
import com.chis.communityhealthis.repository.assistance.AssistanceDao;
import com.chis.communityhealthis.repository.assistancecomment.AssistanceCommentDao;
import com.chis.communityhealthis.service.audit.AuditService;
import com.chis.communityhealthis.utility.AuditConstant;
import com.chis.communityhealthis.utility.BeanComparator;
import com.chis.communityhealthis.utility.DatetimeUtil;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private AssistanceDao assistanceDao;

    @Autowired
    private AssistanceCommentDao assistanceCommentDao;

    @Autowired
    private AuditService auditService;

    @Override
    public List<AppointmentModel> getLoggedInUserAppointments(String username, boolean currentLoggedInUserIsAdmin, Integer appointmentId, String status) {
        List<AppointmentBean> appointmentBeanList = appointmentDao.getUserAppointments(username, currentLoggedInUserIsAdmin, appointmentId, status);
        List<AppointmentModel> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appointmentBeanList)) {
            for (AppointmentBean appointmentBean : appointmentBeanList) {
                list.add(AppointmentModelFactory.createAppointmentModel(appointmentBean));
            }
        }
        return list;
    }

    @Override
    public AppointmentModel getAppointment(Integer appointmentId) throws Exception {
        AppointmentBean appointmentBean = appointmentDao.find(appointmentId);
        if (appointmentBean == null) {
            throw new Exception("Appointment [ID: " + appointmentId + "] was not found.");
        }
        return AppointmentModelFactory.createAppointmentModel(appointmentBean);
    }

    @Override
    public void updateDatetime(UpdateDatetimeForm form) throws Exception {
        AppointmentBean appointmentBean = appointmentDao.find(form.getAppointmentId());
        if (appointmentBean == null) {
            throw new Exception("Appointment [ID: " + form.getAppointmentId().toString() + " ] was not found.");
        } else if (!DatetimeUtil.isEqualDatetime(appointmentBean.getLastUpdatedDate(), form.getAppointmentLastUpdatedDate())) {
            throw new Exception("Appointment has already been updated and may have change(s). Please refresh to get the latest view.");
        }

        Date endTime = calculateEndDatetime(form.getDatetime());
        appointmentBean.setAppointmentStartTime(form.getDatetime());
        appointmentBean.setAppointmentEndTime(endTime);

        boolean isAdmin = adminDao.find(form.getUpdatedBy()) != null;
        String status = isAdmin ? AppointmentBean.APPOINTMENT_STATUS_PENDING_USER : AppointmentBean.APPOINTMENT_STATUS_PENDING_ADMIN;
        appointmentBean.setAppointmentStatus(status);

        if (isAdmin && StringUtils.isNotBlank(appointmentBean.getAdminUsername()) && !StringUtils.equals(appointmentBean.getAdminUsername(), form.getUpdatedBy())) {
            String anotherAdminFullName = appointmentBean.getAdminBean().getFullName();
            throw new Exception("Appointment [ID: " + form.getAppointmentId().toString() + "] is handled by " + anotherAdminFullName + ". Please refresh the page.");
        } else if (!isAdmin && !StringUtils.equals(appointmentBean.getUsername(), form.getUpdatedBy())) {
            throw new Exception("Unauthorized user to reschedule appointment.");
        }

        boolean acceptToHandleAppointment = false;
        if (isAdmin && StringUtils.isBlank(appointmentBean.getAdminUsername())) {
            acceptToHandleAppointment = true;
            appointmentBean.setAdminUsername(form.getUpdatedBy());
        }
        appointmentBean.setLastUpdatedBy(form.getUpdatedBy());
        appointmentBean.setLastUpdatedDate(form.getUpdatedDate());
        appointmentDao.update(appointmentBean);
        AuditBean rescheduleAuditBean;
        if (acceptToHandleAppointment) {
            rescheduleAuditBean = new AuditBean(AuditConstant.MODULE_APPOINTMENT, AuditConstant.formatActionAcceptAndRescheduleAppointment(form.getAppointmentId()),form.getUpdatedBy());
        } else {
            rescheduleAuditBean = new AuditBean(AuditConstant.MODULE_APPOINTMENT, AuditConstant.formatActionRescheduleAppointment(form.getAppointmentId()),form.getUpdatedBy());
        }
        auditService.saveLogs(rescheduleAuditBean, null);

        if (form.getAssistanceId() != null) {
            AssistanceBean assistanceBean = assistanceDao.find(form.getAssistanceId());
            if (assistanceBean != null && StringUtils.isBlank(assistanceBean.getAdminUsername()) && isAdmin) {
                assistanceBean.setAdminUsername(form.getUpdatedBy());
                assistanceBean.setLastUpdatedBy(form.getUpdatedBy());
                assistanceBean.setLastUpdatedDate(form.getUpdatedDate());
                assistanceDao.update(assistanceBean);

                AuditBean acceptToHandleAssistanceAuditBean = new AuditBean(AuditConstant.MODULE_ASSISTANCE, AuditConstant.formatActionAcceptToHandleAssistance(form.getAssistanceId()),form.getUpdatedBy());
                auditService.saveLogs(acceptToHandleAssistanceAuditBean, null);
            }
        }
    }

    @Override
    public void confirmAppointment(ConfirmationForm form) throws Exception {
        AppointmentBean appointmentBean = appointmentDao.find(form.getAppointmentId());
        if (appointmentBean == null) {
            throw new Exception("AppointmentBean [ID: " + form.getAppointmentId().toString() + "] was not found.");
        } else if (!DatetimeUtil.isEqualDatetime(form.getAppointmentLastUpdatedDate(), appointmentBean.getLastUpdatedDate())) {
            throw new Exception("Appointment has already been updated and may have change(s). Please refresh to get the latest view.");
        }

        final String status = appointmentBean.getAppointmentStatus();
        final List<String> ACCEPTABLE_STATUS = Arrays.asList(AppointmentBean.APPOINTMENT_STATUS_PENDING_USER, AppointmentBean.APPOINTMENT_STATUS_PENDING_ADMIN);
        if (!ACCEPTABLE_STATUS.contains(status)) {
            switch (status) {
                case AppointmentBean.APPOINTMENT_STATUS_CONFIRMED:
                    final String handlerFullName = appointmentBean.getAdminBean().getFullName();
                    throw new Exception("Appointment [ID: " + form.getAppointmentId().toString() + "] is already confirmed by " + handlerFullName + ". Please refresh the page.");
                case AppointmentBean.APPOINTMENT_STATUS_CANCELLED:
                    throw new Exception("Appointment [ID: " + form.getAppointmentId().toString() + "] has already been cancelled. Please refresh the page.");
                case AppointmentBean.APPOINTMENT_STATUS_COMPLETED:
                    throw new Exception("Appointment [ID: " + form.getAppointmentId().toString() + "] has already been completed. Please refresh the page.");
            }
        }

        boolean isAdmin = adminDao.find(form.getConfirmedBy()) != null;
        AppointmentBean clashedAppointment = appointmentDao.findClashedAppointment(form.getConfirmedBy(), isAdmin, appointmentBean.getAppointmentStartTime());
        if (clashedAppointment != null) {
            throw new Exception("You've confirmed another appointment [ID: " + clashedAppointment.getAppointmentId().toString() + "] that clashed with this appointment.");
        }

        if (isPendingUser(status)) {
            if (isAdmin) {
                throw new Exception("Appointment is pending user's confirmation.");
            } else if (!StringUtils.equals(form.getConfirmedBy(), appointmentBean.getUsername())) {
                throw new Exception("Unauthorized to confirm appointment.");
            }
        } else if (isPendingAdmin(status)) {
            if (!isAdmin) {
                throw new Exception("Appointment is pending admin's confirmation.");
            } else if (StringUtils.isNotBlank(appointmentBean.getAdminUsername()) && !StringUtils.equals(appointmentBean.getAdminUsername(), form.getConfirmedBy())) {
                final String handlerName = appointmentBean.getAdminBean().getFullName();
                throw new Exception("Appointment is being handled by " + handlerName + ".");
            }
        }

        if (StringUtils.isBlank(appointmentBean.getAdminUsername()) && isAdmin) {
            appointmentBean.setAdminUsername(form.getConfirmedBy());
        }
        appointmentBean.setAppointmentStatus(AppointmentBean.APPOINTMENT_STATUS_CONFIRMED);
        appointmentBean.setLastUpdatedBy(form.getConfirmedBy());
        appointmentBean.setLastUpdatedDate(form.getConfirmedDate());
        appointmentDao.update(appointmentBean);
        AuditBean confirmationAuditBean = new AuditBean(AuditConstant.MODULE_APPOINTMENT, AuditConstant.formatActionConfirmAppointment(appointmentBean.getAppointmentId()), form.getConfirmedBy());
        auditService.saveLogs(confirmationAuditBean, null);

        if (form.getAssistanceId() != null) {
            AssistanceBean assistanceBean = assistanceDao.find(form.getAssistanceId());
            if (assistanceBean != null && StringUtils.isBlank(assistanceBean.getAdminUsername()) && isAdmin) {
                assistanceBean.setAdminUsername(form.getConfirmedBy());
                assistanceBean.setLastUpdatedBy(form.getConfirmedBy());
                assistanceBean.setLastUpdatedDate(form.getConfirmedDate());
                assistanceDao.update(assistanceBean);

                AuditBean acceptToHandleAssistanceAuditBean = new AuditBean(AuditConstant.MODULE_ASSISTANCE, AuditConstant.formatActionAcceptToHandleAssistance(form.getAssistanceId()),form.getConfirmedBy());
                auditService.saveLogs(acceptToHandleAssistanceAuditBean, null);
            }
        }
    }

    @Override
    public List<AppointmentModel> getConfirmedAppointments(String username, boolean isAdmin, Date date) {
        List<AppointmentBean> appointmentBeanList = appointmentDao.getConfirmedAppointments(username, isAdmin, date);
        List<AppointmentModel> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appointmentBeanList)) {
            for (AppointmentBean appointmentBean : appointmentBeanList) {
                list.add(AppointmentModelFactory.createAppointmentModel(appointmentBean));
            }
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public void updateAppointmentStatus(UpdateAppointmentStatusForm form) throws Exception {
        AppointmentBean appointmentBean = appointmentDao.find(form.getAppointmentId());
        if (appointmentBean == null) {
            throw new Exception("Appointment [ID: " + form.getAppointmentId() + "] was not found.");
        }
        AppointmentBean appointmentBeanDeepCopy = SerializationUtils.clone(appointmentBean);
        appointmentBean.setAppointmentStatus(form.getAppointmentStatus());
        appointmentBean.setLastUpdatedBy(form.getSubmittedBy());
        appointmentBean.setLastUpdatedDate(form.getSubmittedDate());
        appointmentDao.update(appointmentBean);

        BeanComparator appointmentBeanComparator = new BeanComparator(createPureAppointmentBean(appointmentBeanDeepCopy), createPureAppointmentBean(appointmentBean));
        AuditBean appointmentAuditBean = new AuditBean(AuditConstant.MODULE_APPOINTMENT, AuditConstant.formatActionUpdateAppointmentStatus(appointmentBean.getAppointmentId()), form.getSubmittedBy());
        List<AuditActionBean> appointmentAuditActions = new ArrayList<>();
        if (appointmentBeanComparator.hasChanges()) {
            appointmentAuditActions.add(new AuditActionBean(appointmentBeanComparator.toPrettyString()));
        }
        auditService.saveLogs(appointmentAuditBean, appointmentAuditActions);

        if (form.getAssistanceId() != null) {
            AssistanceBean assistanceBean = assistanceDao.find(form.getAssistanceId());
            AssistanceBean assistanceBeanDeepCopy = SerializationUtils.clone(assistanceBean);
            assistanceBean.setAssistanceStatus(form.getAssistanceStatus());
            assistanceBean.setLastUpdatedBy(form.getSubmittedBy());
            assistanceBean.setLastUpdatedDate(form.getSubmittedDate());
            if (StringUtils.isBlank(assistanceBean.getAdminUsername())) {
                assistanceBean.setAdminUsername(form.getSubmittedBy());
            }
            assistanceDao.update(assistanceBean);
            BeanComparator assistanceBeanComparator = new BeanComparator(createPureAssistanceBean(assistanceBeanDeepCopy), createPureAssistanceBean(assistanceBean));
            AuditBean assistanceAuditBean = new AuditBean(AuditConstant.MODULE_APPOINTMENT, AuditConstant.formatActionUpdateAssistanceStatus(assistanceBean.getAssistanceId()), form.getSubmittedBy());
            List<AuditActionBean> assistanceAuditActions = new ArrayList<>();
            if (assistanceBeanComparator.hasChanges()) {
                assistanceAuditActions.add(new AuditActionBean(assistanceBeanComparator.toPrettyString()));
            }
            auditService.saveLogs(assistanceAuditBean, assistanceAuditActions);

            AssistanceCommentBean assistanceCommentBean = new AssistanceCommentBean();
            assistanceCommentBean.setAssistanceId(form.getAssistanceId());
            String desc = "";
            if (StringUtils.equals(AssistanceBean.STATUS_ACCEPTED, assistanceBean.getAssistanceStatus())) {
                desc = "I've accepted this assistance request. (Reason: " + form.getReason() + ")";
            } else if (StringUtils.equals(AssistanceBean.STATUS_REJECTED, assistanceBean.getAssistanceStatus())) {
                desc = "I've rejected this assistance request. (Reason: " + form.getReason() + ")";
            } else if (StringUtils.equals(AssistanceBean.STATUS_CANCELLED, assistanceBean.getAssistanceStatus())) {
                desc = "This assistance request has been cancelled. (Reason: " + form.getReason() + ")";
            }
            assistanceCommentBean.setCommentDesc(desc);
            assistanceCommentBean.setCreatedDate(form.getSubmittedDate());
            assistanceCommentBean.setCreatedBy(form.getSubmittedBy());
            assistanceCommentDao.add(assistanceCommentBean);
        }
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

    private AppointmentBean createPureAppointmentBean(AppointmentBean appointmentBean) {
        AppointmentBean bean = new AppointmentBean();
        bean.setAppointmentId(appointmentBean.getAppointmentId());
        bean.setAppointmentPurpose(appointmentBean.getAppointmentPurpose());
        bean.setAppointmentStartTime(appointmentBean.getAppointmentStartTime());
        bean.setAppointmentEndTime(appointmentBean.getAppointmentEndTime());
        bean.setAppointmentStatus(appointmentBean.getAppointmentStatus());
        bean.setCreatedBy(appointmentBean.getCreatedBy());
        bean.setCreatedDate(appointmentBean.getCreatedDate());
        bean.setUsername(appointmentBean.getUsername());
        bean.setAdminUsername(appointmentBean.getAdminUsername());
        bean.setLastUpdatedBy(appointmentBean.getLastUpdatedBy());
        bean.setLastUpdatedDate(appointmentBean.getLastUpdatedDate());
        return bean;
    }

    private Date calculateEndDatetime(Date startDatetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDatetime);
        calendar.add(Calendar.MINUTE, 30);
        return calendar.getTime();
    }

    private boolean isPendingAdmin(String status) {
        return StringUtils.equals(status, AppointmentBean.APPOINTMENT_STATUS_PENDING_ADMIN);
    }

    private boolean isPendingUser(String status) {
        return StringUtils.equals(status, AppointmentBean.APPOINTMENT_STATUS_PENDING_USER);
    }
}
