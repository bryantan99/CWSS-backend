package com.chis.communityhealthis.service.appointment;

import com.chis.communityhealthis.bean.AppointmentBean;
import com.chis.communityhealthis.model.appointment.AppointmentModel;
import com.chis.communityhealthis.model.appointment.ConfirmationForm;
import com.chis.communityhealthis.model.appointment.ScheduleAppointmentForm;
import com.chis.communityhealthis.model.appointment.UpdateDatetimeForm;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.repository.appointment.AppointmentDao;
import com.chis.communityhealthis.utility.DatetimeUtil;
import io.jsonwebtoken.lang.Assert;
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

    @Override
    public List<AppointmentModel> getPendingAppointments(Integer appointmentId) {
        List<AppointmentBean> appointmentBeanList = appointmentDao.getPendingAdminAppointmentsWithoutAdminUsername(appointmentId);
        List<AppointmentModel> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appointmentBeanList)) {
            for (AppointmentBean appointmentBean : appointmentBeanList) {
                list.add(toAppointmentModel(appointmentBean));
            }
        }
        return list;
    }

    @Override
    public List<AppointmentModel> getLoggedInUserAppointments(String username, boolean currentLoggedInUserIsAdmin, Integer appointmentId, String status) {
        List<AppointmentBean> appointmentBeanList = appointmentDao.getUserAppointments(username, currentLoggedInUserIsAdmin, appointmentId, status);
        List<AppointmentModel> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appointmentBeanList)) {
            for (AppointmentBean appointmentBean : appointmentBeanList) {
                list.add(toAppointmentModel(appointmentBean));
            }
        }
        return list;
    }

    @Override
    public void cancelAppointment(Integer appointmentId, String actionMakerUsername) throws Exception {
        AppointmentBean appointmentBean = appointmentDao.find(appointmentId);
        Assert.notNull(appointmentBean, "AppointmentBean [ID: " + appointmentId.toString() + "] was not found.");

        boolean isAdmin = adminDao.find(actionMakerUsername) != null;
        if (!isAdmin && !StringUtils.equals(actionMakerUsername, appointmentBean.getUsername())) {
            throw new Exception("User is unauthorized to cancel appointment [ID: " + appointmentId + ".");
        }

        appointmentBean.setAppointmentStatus(AppointmentBean.APPOINTMENT_STATUS_CANCELLED);
        appointmentBean.setLastUpdatedBy(actionMakerUsername);
        appointmentBean.setLastUpdatedDate(new Date());
        appointmentDao.update(appointmentBean);
    }

    @Override
    public AppointmentModel getAppointment(Integer appointmentId) throws Exception {
        AppointmentBean appointmentBean = appointmentDao.find(appointmentId);
        if (appointmentBean == null) {
            throw new Exception("Appointment [ID: " + appointmentId + "] was not found.");
        }
        return toAppointmentModel(appointmentBean);
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

        if (isAdmin && StringUtils.isBlank(appointmentBean.getAdminUsername())) {
            appointmentBean.setAdminUsername(form.getUpdatedBy());
        }
        appointmentBean.setLastUpdatedBy(form.getUpdatedBy());
        appointmentBean.setLastUpdatedDate(form.getUpdatedDate());
        appointmentDao.update(appointmentBean);
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
    }

    @Override
    public Integer scheduleAppointment(ScheduleAppointmentForm form) {
        boolean isAdmin = adminDao.find(form.getCreatedBy()) != null;
        String status = isAdmin ? AppointmentBean.APPOINTMENT_STATUS_PENDING_USER : AppointmentBean.APPOINTMENT_STATUS_PENDING_ADMIN;

        AppointmentBean bean = new AppointmentBean();
        bean.setAppointmentPurpose(form.getPurpose());
        bean.setAppointmentStartTime(form.getDatetime());
        bean.setAppointmentEndTime(calculateEndDatetime(form.getDatetime()));
        bean.setAppointmentStatus(status);
        bean.setCreatedBy(form.getCreatedBy());
        bean.setCreatedDate(new Date());
        bean.setAdminUsername(form.getAdminUsername());
        bean.setUsername(isAdmin ? form.getUsername() : form.getCreatedBy());

        return appointmentDao.add(bean);
    }

    @Override
    public List<AppointmentModel> getConfirmedAppointments(String username, boolean isAdmin, Date date) {
        List<AppointmentBean> appointmentBeanList = appointmentDao.getConfirmedAppointments(username, isAdmin, date);
        List<AppointmentModel> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appointmentBeanList)) {
            for (AppointmentBean appointmentBean : appointmentBeanList) {
                list.add(toAppointmentModel(appointmentBean));
            }
        }
        Collections.sort(list);
        return list;
    }

    private Date calculateEndDatetime(Date startDatetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDatetime);
        calendar.add(Calendar.MINUTE, 30);
        return calendar.getTime();
    }

    private AppointmentModel toAppointmentModel(AppointmentBean appointmentBean) {
        AppointmentModel model = new AppointmentModel();
        model.setAppointmentId(appointmentBean.getAppointmentId());
        model.setPurpose(appointmentBean.getAppointmentPurpose());
        model.setStartDatetime(appointmentBean.getAppointmentStartTime());
        model.setEndDatetime(appointmentBean.getAppointmentEndTime());
        model.setStatus(appointmentBean.getAppointmentStatus());
        if (appointmentBean.getCommunityUserBean() != null) {
            model.setUsername(appointmentBean.getUsername());
            model.setUserFullName(appointmentBean.getCommunityUserBean().getFullName());
        }
        if (appointmentBean.getAdminBean() != null) {
            model.setAdminUsername(appointmentBean.getAdminUsername());
            model.setAdminFullName(appointmentBean.getAdminBean().getFullName());
        }
        if (appointmentBean.getLastUpdatedDate() != null) {
            model.setLastUpdatedDate(appointmentBean.getLastUpdatedDate());
        }
        return model;
    }

    private boolean isPendingAdmin(String status) {
        return StringUtils.equals(status, AppointmentBean.APPOINTMENT_STATUS_PENDING_ADMIN);
    }

    private boolean isPendingUser(String status) {
        return StringUtils.equals(status, AppointmentBean.APPOINTMENT_STATUS_PENDING_USER);
    }
}
