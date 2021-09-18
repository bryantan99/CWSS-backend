package com.chis.communityhealthis.repository;

import com.chis.communityhealthis.bean.PostMediaBean;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PostMediaDaoImpl extends GenericDaoImpl<PostMediaBean, Integer> implements PostMediaDao {
    @Override
    public List<PostMediaBean> findMedias(Integer postId) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<PostMediaBean> criteriaQuery = criteriaBuilder.createQuery(PostMediaBean.class);
        Root<PostMediaBean> root = criteriaQuery.from(PostMediaBean.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("postId"), postId));

        Query<PostMediaBean> query = currentSession().createQuery(criteriaQuery);
        return query.list();
    }
}
