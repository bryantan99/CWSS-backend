package com.chis.communityhealthis.repository.assistance;

import com.chis.communityhealthis.bean.AssistanceBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class AssistanceDaoImpl extends GenericDaoImpl<AssistanceBean, Integer> implements AssistanceDao {

    @Override
    public List<AssistanceBean> findUserAssistanceRecords(String username) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AssistanceBean> criteriaQuery = criteriaBuilder.createQuery(AssistanceBean.class);
        Root<AssistanceBean> root = criteriaQuery.from(AssistanceBean.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("username"), username));
        Query<AssistanceBean> query = currentSession().createQuery(criteriaQuery);
        return query.list();
    }
}
