package com.chis.communityhealthis.repository.appointment;

import com.chis.communityhealthis.bean.AppointmentBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface AppointmentDao extends GenericDao<AppointmentBean, Integer> {
    List<AppointmentBean> getUserAppointments(String username);
}