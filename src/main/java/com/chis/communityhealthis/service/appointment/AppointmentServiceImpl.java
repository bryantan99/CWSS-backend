package com.chis.communityhealthis.service.appointment;

import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.bean.AppointmentBean;
import com.chis.communityhealthis.model.appointment.UpdateDatetimeForm;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.repository.appointment.AppointmentDao;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new Exception(actionMakerUsername + " is unauthorized to cancel appointment [ID: " + appointmentId.toString() + ".");
        }

        appointmentBean.setAppointmentStatus("cancelled");
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

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(form.getDatetime());
        endCalendar.add(Calendar.HOUR_OF_DAY, 1);
        appointmentBean.setAppointmentStartTime(form.getDatetime());
        appointmentBean.setAppointmentEndTime(endCalendar.getTime());

        appointmentBean.setLastUpdatedBy(form.getUpdatedBy());
        appointmentBean.setLastUpdatedDate(form.getUpdatedDate());

        appointmentDao.saveOrUpdate(appointmentBean);
    }
}
