package com.chis.communityhealthis.repository.assistancecomment;

import com.chis.communityhealthis.bean.AssistanceCommentBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AssistanceCommentDaoImpl extends GenericDaoImpl<AssistanceCommentBean, Integer> implements AssistanceCommentDao {

    @Override
    public List<AssistanceCommentBean> findComments(Integer assistanceId) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AssistanceCommentBean> criteriaQuery = criteriaBuilder.createQuery(AssistanceCommentBean.class);
        Root<AssistanceCommentBean> root = criteriaQuery.from(AssistanceCommentBean.class);
        root.fetch("adminBean", JoinType.LEFT);
        root.fetch("communityUserBean", JoinType.LEFT);

        List<Order> orderList = new ArrayList<>();
        orderList.add(criteriaBuilder.desc(root.get("createdDate")));

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("assistanceId"), assistanceId)).orderBy(orderList);
        Query<AssistanceCommentBean> query = currentSession().createQuery(criteriaQuery);
        return query.list();
    }
}
