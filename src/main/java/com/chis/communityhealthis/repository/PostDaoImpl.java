package com.chis.communityhealthis.repository;

import com.chis.communityhealthis.bean.PostBean;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDaoImpl extends GenericDaoImpl<PostBean, Integer> implements PostDao {

    @Override
    public List<PostBean> getPosts() {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<PostBean> criteriaQuery = criteriaBuilder.createQuery(PostBean.class);
        Root<PostBean> root = criteriaQuery.from(PostBean.class);
        root.fetch("adminBean", JoinType.INNER);

        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.desc(root.get("createdDate")));

        criteriaQuery.select(root).orderBy(orderList);
        Query<PostBean> query = currentSession().createQuery(criteriaQuery);
        return query.list();
    }

    @Override
    public List<PostBean> getPostsWithMedia() {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<PostBean> criteriaQuery = criteriaBuilder.createQuery(PostBean.class);
        Root<PostBean> root = criteriaQuery.from(PostBean.class);
        root.fetch("adminBean", JoinType.INNER);
        root.fetch("postMediaBeanSet", JoinType.LEFT);

        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.desc(root.get("createdDate")));

        criteriaQuery.select(root).distinct(true).orderBy(orderList);
        Query<PostBean> query = currentSession().createQuery(criteriaQuery);
        return query.list();
    }

    @Override
    public PostBean getPostWithMedia(Integer postId) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<PostBean> criteriaQuery = criteriaBuilder.createQuery(PostBean.class);
        Root<PostBean> root = criteriaQuery.from(PostBean.class);
        root.fetch("adminBean", JoinType.INNER);
        root.fetch("postMediaBeanSet", JoinType.LEFT);

        criteriaQuery.select(root).distinct(true).where(criteriaBuilder.equal(root.get("postId"), postId));
        Query<PostBean> query = currentSession().createQuery(criteriaQuery);
        return query.uniqueResult();
    }
}
