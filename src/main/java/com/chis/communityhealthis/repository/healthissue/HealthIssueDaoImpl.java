package com.chis.communityhealthis.repository.healthissue;

import com.chis.communityhealthis.bean.HealthIssueBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HealthIssueDaoImpl extends GenericDaoImpl<HealthIssueBean, Integer> implements HealthIssueDao {

    @Override
    public List<HealthIssueBean> findHealthIssueBeans(String username) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<HealthIssueBean> criteriaQuery = criteriaBuilder.createQuery(HealthIssueBean.class);
        Root<HealthIssueBean> root = criteriaQuery.from(HealthIssueBean.class);
        root.fetch("diseaseBean", JoinType.INNER);
        root.fetch("adminBean", JoinType.LEFT);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("username"), username));
        Query<HealthIssueBean> query = currentSession().createQuery(criteriaQuery);
        return query.list();
    }

    @Override
    public List<HealthIssueBean> getUsersHealthIssues(List<String> usernameList) {
        if (CollectionUtils.isEmpty(usernameList)) {
            return new ArrayList<>();
        }

        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<HealthIssueBean> criteriaQuery = criteriaBuilder.createQuery(HealthIssueBean.class);
        Root<HealthIssueBean> root = criteriaQuery.from(HealthIssueBean.class);

        CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("username"));
        for (String username : usernameList) {
            inClause.value(username);
        }

        criteriaQuery.select(root).where(inClause);

        Query<HealthIssueBean> query = currentSession().createQuery(criteriaQuery);
        return query.getResultList();
    }
}
