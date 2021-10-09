package com.chis.communityhealthis.repository.occupation;

import com.chis.communityhealthis.bean.OccupationBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OccupationDaoImpl extends GenericDaoImpl<OccupationBean, String> implements OccupationDao {
    @Override
    public List<OccupationBean> getUsersOccupation(List<String> usernameList) {
        if (CollectionUtils.isEmpty(usernameList)) {
            return new ArrayList<>();
        }

        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<OccupationBean> criteriaQuery = criteriaBuilder.createQuery(OccupationBean.class);
        Root<OccupationBean> root = criteriaQuery.from(OccupationBean.class);

        CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("username"));
        for (String username : usernameList) {
            inClause.value(username);
        }

        criteriaQuery.select(root).where(inClause);

        Query<OccupationBean> query = currentSession().createQuery(criteriaQuery);
        return query.getResultList();
    }
}
