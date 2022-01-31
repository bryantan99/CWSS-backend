package com.chis.communityhealthis.repository.holiday;

import com.chis.communityhealthis.bean.HolidayBean;
import com.chis.communityhealthis.bean.RefreshTokenBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Repository
public class HolidayDaoImpl extends GenericDaoImpl<HolidayBean, Integer> implements HolidayDao {

    @Override
    public HolidayBean findHolidayByDate(Date date) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<HolidayBean> criteriaQuery = criteriaBuilder.createQuery(HolidayBean.class);
        Root<HolidayBean> root = criteriaQuery.from(HolidayBean.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("holidayDate"), date));
        Query<HolidayBean> query = currentSession().createQuery(criteriaQuery);
        return query.uniqueResult();
    }

    @Override
    public List<HolidayBean> findHolidayByYear(String year) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<HolidayBean> criteriaQuery = criteriaBuilder.createQuery(HolidayBean.class);
        Root<HolidayBean> root = criteriaQuery.from(HolidayBean.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("holidayYear"), year));
        Query<HolidayBean> query = currentSession().createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public int deleteHolidayById(Integer holidayId) {
        CriteriaBuilder criteriaBuilder  = currentSession().getCriteriaBuilder();
        CriteriaDelete<HolidayBean> query = criteriaBuilder.createCriteriaDelete(HolidayBean.class);
        Root<HolidayBean> root = query.from(HolidayBean.class);
        query.where(criteriaBuilder.equal(root.get("holidayId"), holidayId));

        return currentSession().createQuery(query).executeUpdate();
    }
}
