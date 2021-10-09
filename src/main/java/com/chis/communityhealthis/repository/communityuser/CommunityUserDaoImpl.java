package com.chis.communityhealthis.repository.communityuser;

import com.chis.communityhealthis.bean.CommunityUserBean;
import com.chis.communityhealthis.model.filter.CommunityUserBeanQuery;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommunityUserDaoImpl extends GenericDaoImpl<CommunityUserBean, String> implements CommunityUserDao {
    @Override
    public List<CommunityUserBean> getCommunityUsers(CommunityUserBeanQuery filter) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<CommunityUserBean> criteriaQuery = criteriaBuilder.createQuery(CommunityUserBean.class);
        Root<CommunityUserBean> root = criteriaQuery.from(CommunityUserBean.class);

        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(filter.getName())) {
            predicates.add(criteriaBuilder.like(root.get("fullName"), "%" + filter.getName() + "%"));
        }

        if (StringUtils.isNotBlank(filter.getNric())) {
            predicates.add(criteriaBuilder.like(root.get("nric"), "%" + filter.getNric() + "%"));
        }

        if (StringUtils.isNotBlank(filter.getGender())) {
            predicates.add(criteriaBuilder.equal(root.get("gender"), filter.getGender()));
        }

        if (StringUtils.isNotBlank(filter.getEthnic())) {
            predicates.add(criteriaBuilder.equal(root.get("ethnic"), filter.getEthnic()));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        Query<CommunityUserBean> query = currentSession().createQuery(criteriaQuery);
        return query.getResultList();
    }
}
