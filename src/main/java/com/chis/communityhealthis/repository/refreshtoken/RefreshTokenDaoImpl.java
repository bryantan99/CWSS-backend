package com.chis.communityhealthis.repository.refreshtoken;

import com.chis.communityhealthis.bean.RefreshTokenBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class RefreshTokenDaoImpl extends GenericDaoImpl<RefreshTokenBean, Long> implements RefreshTokenDao {

    @Override
    public RefreshTokenBean findByToken(String token) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<RefreshTokenBean> criteriaQuery = criteriaBuilder.createQuery(RefreshTokenBean.class);
        Root<RefreshTokenBean> root = criteriaQuery.from(RefreshTokenBean.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("token"), token));

        Query<RefreshTokenBean> query = currentSession().createQuery(criteriaQuery);
        return query.uniqueResult();
    }

    @Override
    public RefreshTokenBean findByUsername(String username) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<RefreshTokenBean> criteriaQuery = criteriaBuilder.createQuery(RefreshTokenBean.class);
        Root<RefreshTokenBean> root = criteriaQuery.from(RefreshTokenBean.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("username"), username));

        Query<RefreshTokenBean> query = currentSession().createQuery(criteriaQuery);
        return query.uniqueResult();
    }

    @Override
    public int deleteByUsername(String username) {
        CriteriaBuilder criteriaBuilder  = currentSession().getCriteriaBuilder();
        CriteriaDelete<RefreshTokenBean> query = criteriaBuilder.createCriteriaDelete(RefreshTokenBean.class);
        Root<RefreshTokenBean> root = query.from(RefreshTokenBean.class);
        query.where(criteriaBuilder.equal(root.get("username"), username));

        return currentSession().createQuery(query).executeUpdate();
    }
}
