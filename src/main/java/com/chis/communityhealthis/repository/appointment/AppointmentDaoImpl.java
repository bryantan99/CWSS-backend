package com.chis.communityhealthis.repository.appointment;

import com.chis.communityhealthis.bean.AppointmentBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
}
