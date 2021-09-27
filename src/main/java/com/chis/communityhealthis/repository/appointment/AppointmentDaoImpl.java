package com.chis.communityhealthis.repository.appointment;

import com.chis.communityhealthis.bean.AppointmentBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class AppointmentDaoImpl extends GenericDaoImpl<AppointmentBean, Integer> implements AppointmentDao {

    @Override
    public List<AppointmentBean> getUserAppointments(String username) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AppointmentBean> criteriaQuery = criteriaBuilder.createQuery(AppointmentBean.class);
        Root<AppointmentBean> root = criteriaQuery.from(AppointmentBean.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("username"), username));
        Query<AppointmentBean> query = currentSession().createQuery(criteriaQuery);
        return query.list();
    }

    @Override
    public List<AppointmentBean> getAdminAppointments(String adminUsername, Date date) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AppointmentBean> criteriaQuery = criteriaBuilder.createQuery(AppointmentBean.class);
        Root<AppointmentBean> root = criteriaQuery.from(AppointmentBean.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("adminUsername"), adminUsername));
        if (date != null) {
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(date);
            endCalendar.set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            Date endDate = endCalendar.getTime();
            predicates.add(criteriaBuilder.between(root.<Date>get("appointmentStartTime"), date, endDate));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        Query<AppointmentBean> query = currentSession().createQuery(criteriaQuery);
        return query.list();
    }

    @Override
    public List<AppointmentBean> getConfirmedAppointments(String username, boolean isAdmin, Date date) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AppointmentBean> criteriaQuery = criteriaBuilder.createQuery(AppointmentBean.class);
        Root<AppointmentBean> root = criteriaQuery.from(AppointmentBean.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("appointmentStatus"), AppointmentBean.APPOINTMENT_STATUS_CONFIRMED));

        if (isAdmin) {
            predicates.add(criteriaBuilder.equal(root.get("adminUsername"), username));
        } else {
            predicates.add(criteriaBuilder.equal(root.get("username"), username));
        }

        if (date != null) {
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(date);
            endCalendar.set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            Date endDate = endCalendar.getTime();
            predicates.add(criteriaBuilder.between(root.<Date>get("appointmentStartTime"), date, endDate));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        Query<AppointmentBean> query = currentSession().createQuery(criteriaQuery);
        return query.list();
    }
}
