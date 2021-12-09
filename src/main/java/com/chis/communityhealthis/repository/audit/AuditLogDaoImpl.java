package com.chis.communityhealthis.repository.audit;

import com.chis.communityhealthis.bean.AuditBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AuditLogDaoImpl extends GenericDaoImpl<AuditBean, Integer> implements AuditLogDao {

    @Override
    public List<AuditBean> getAuditBeans(String moduleName) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AuditBean> criteriaQuery = criteriaBuilder.createQuery(AuditBean.class);
        Root<AuditBean> root = criteriaQuery.from(AuditBean.class);

        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(moduleName)) {
            predicates.add(criteriaBuilder.equal(root.get("module"), moduleName));
        }

        List<Order> orderList = new ArrayList<>();
        orderList.add(criteriaBuilder.desc(root.get("actionDate")));

        criteriaQuery.select(root).distinct(true).where(predicates.toArray(new Predicate[]{})).orderBy(orderList);
        Query<AuditBean> query = currentSession().createQuery(criteriaQuery);
        return query.list();
    }

}
