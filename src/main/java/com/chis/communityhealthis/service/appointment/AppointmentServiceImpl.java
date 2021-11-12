package com.chis.communityhealthis.service.appointment;

import com.chis.communityhealthis.bean.AppointmentBean;
import com.chis.communityhealthis.model.appointment.ConfirmationForm;
import com.chis.communityhealthis.model.appointment.ScheduleAppointmentForm;
import com.chis.communityhealthis.model.appointment.UpdateDatetimeForm;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.repository.appointment.AppointmentDao;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private AdminDao adminDao;

    @Override
    public List<AppointmentBean> getAllAppointments() {
        return appointmentDao.getAll();
    }

    @Override
    public List<AppointmentBean> getUserAppointments(String username) {
        return appointmentDao.getUserAppointments(username);
    }

    @Override
    public void cancelAppointment(Integer appointmentId, String actionMakerUsername) throws Exception {
        AppointmentBean appointmentBean = appointmentDao.find(appointmentId);
        Assert.notNull(appointmentBean, "AppointmentBean [ID: " + appointmentId.toString() + "] was not found.");

        boolean isAdmin = adminDao.find(actionMakerUsername) != null;

        if (!isAdmin && !StringUtils.equals(actionMakerUsername, appointmentBean.getUsername())) {
            throw new Exception(actionMakerUsername + " is unauthorized to cancel appointment [ID: " + appointmentId + ".");
        }

        appointmentBean.setAppointmentStatus(AppointmentBean.APPOINTMENT_STATUS_CANCELLED);
        appointmentBean.setLastUpdatedBy(actionMakerUsername);
        appointmentBean.setLastUpdatedDate(new Date());
        appointmentDao.saveOrUpdate(appointmentBean);
    }

    @Override
    public AppointmentBean getAppointment(Integer appointmentId) {
        return appointmentDao.find(appointmentId);
    }

    @Override
    public void updateDatetime(UpdateDatetimeForm form) {
        AppointmentBean appointmentBean = appointmentDao.find(form.getAppointmentId());
        Assert.notNull(appointmentBean, "AppointmentBean [ID: " + form.getAppointmentId().toString() + " ] was not found.");

        Date endTime = calculateEndDatetime(form.getDatetime());
        appointmentBean.setAppointmentStartTime(form.getDatetime());
        appointmentBean.setAppointmentEndTime(endTime);

        boolean isAdmin = adminDao.find(form.getUpdatedBy()) != null;
        String status = isAdmin ? AppointmentBean.APPOINTMENT_STATUS_PENDING_USER : AppointmentBean.APPOINTMENT_STATUS_PENDING_ADMIN;
        appointmentBean.setAppointmentStatus(status);

        appointmentBean.setLastUpdatedBy(form.getUpdatedBy());
        appointmentBean.setLastUpdatedDate(form.getUpdatedDate());

        appointmentDao.saveOrUpdate(appointmentBean);
    }

    @Override
    public void confirmAppointment(ConfirmationForm form) {
        AppointmentBean appointmentBean = appointmentDao.find(form.getAppointmentId());
        Assert.notNull(appointmentBean, "AppointmentBean [ID: " + form.getAppointmentId().toString() + " ] was not found.");

        boolean isAdmin = adminDao.find(form.getConfirmedBy()) != null;
        Assert.isTrue(isAdmin || StringUtils.equals(form.getConfirmedBy(), appointmentBean.getUsername()), "Unauthorized user to confirm appointment.");

        appointmentBean.setAppointmentStatus(AppointmentBean.APPOINTMENT_STATUS_CONFIRMED);
        appointmentBean.setLastUpdatedBy(form.getConfirmedBy());
        appointmentBean.setLastUpdatedDate(form.getConfirmedDate());

        appointmentDao.saveOrUpdate(appointmentBean);
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
    public List<AppointmentBean> getConfirmedAppointments(String username, boolean isAdmin, Date date) {
        return appointmentDao.getConfirmedAppointments(username, isAdmin, date);
    }

    private Date calculateEndDatetime(Date startDatetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDatetime);
        calendar.add(Calendar.MINUTE, 30);
        return calendar.getTime();
    }
}
