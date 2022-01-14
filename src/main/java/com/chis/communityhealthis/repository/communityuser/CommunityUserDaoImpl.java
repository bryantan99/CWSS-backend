package com.chis.communityhealthis.repository.communityuser;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.model.filter.CommunityUserBeanQuery;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommunityUserDaoImpl extends GenericDaoImpl<CommunityUserBean, String> implements CommunityUserDao {
    @Override
    public List<CommunityUserBean> getCommunityUsers(CommunityUserBeanQuery filter) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<CommunityUserBean> criteriaQuery = criteriaBuilder.createQuery(CommunityUserBean.class);
        Root<CommunityUserBean> root = criteriaQuery.from(CommunityUserBean.class);

        Fetch<CommunityUserBean, AddressBean> addressBeanFetch = root.fetch("addressBean", JoinType.LEFT);
        Fetch<AddressBean, ZoneBean> zoneBeanFetch = addressBeanFetch.fetch("zoneBean", JoinType.LEFT);
        Fetch<CommunityUserBean, OccupationBean> occupationBeanFetch = root.fetch("occupationBean", JoinType.LEFT);
        Fetch<CommunityUserBean, HealthIssueBean> healthIssueBeanFetch = root.fetch("healthIssueBeans", JoinType.LEFT);
        Fetch<HealthIssueBean, AdminBean> adminBeanFetch = healthIssueBeanFetch.fetch("adminBean", JoinType.LEFT);
        Fetch<HealthIssueBean, DiseaseBean> diseaseBeanFetch = healthIssueBeanFetch.fetch("diseaseBean", JoinType.LEFT);
        Fetch<CommunityUserBean, AdminBean> blockedByAdminBeanFetch = root.fetch("blockedByAdminBean", JoinType.LEFT);

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

        if (filter.getDiseaseId() != null) {
            Join<CommunityUserBean, HealthIssueBean> healthIssueBeanJoin = (Join<CommunityUserBean, HealthIssueBean>) healthIssueBeanFetch;
            predicates.add(criteriaBuilder.equal(healthIssueBeanJoin.get("diseaseId"), filter.getDiseaseId()));
        }

        if (filter.getZoneId() != null) {
            Join<AddressBean, ZoneBean> zoneBeanJoin = (Join<AddressBean, ZoneBean>) zoneBeanFetch;
            predicates.add(criteriaBuilder.equal(zoneBeanJoin.get("zoneId"), filter.getZoneId()));
        }

        return getResults(criteriaQuery, root, predicates);
    }

    @Override
    public CommunityUserBean getCommunityUser(String username) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<CommunityUserBean> criteriaQuery = criteriaBuilder.createQuery(CommunityUserBean.class);
        Root<CommunityUserBean> root = criteriaQuery.from(CommunityUserBean.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("username"), username));

        fetchTables(root);
        return getResult(criteriaQuery, root, predicates);
    }

    @Override
    public CommunityUserBean getCommunityUserByNric(String nric) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<CommunityUserBean> criteriaQuery = criteriaBuilder.createQuery(CommunityUserBean.class);
        Root<CommunityUserBean> root = criteriaQuery.from(CommunityUserBean.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("nric"), nric));

        fetchTables(root);
        return getResult(criteriaQuery, root, predicates);
    }

    @Override
    public List<CommunityUserBean> getAllCommunityUsers() {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<CommunityUserBean> criteriaQuery = criteriaBuilder.createQuery(CommunityUserBean.class);
        Root<CommunityUserBean> root = criteriaQuery.from(CommunityUserBean.class);

        fetchTables(root);
        return getResults(criteriaQuery, root, null);
    }

    private void fetchTables(Root<CommunityUserBean> root) {
        Fetch<CommunityUserBean, AddressBean> addressBeanFetch = root.fetch("addressBean", JoinType.LEFT);
        Fetch<AddressBean, ZoneBean> zoneBeanFetch = addressBeanFetch.fetch("zoneBean", JoinType.LEFT);
        Fetch<CommunityUserBean, OccupationBean> occupationBeanFetch = root.fetch("occupationBean", JoinType.LEFT);
        Fetch<CommunityUserBean, HealthIssueBean> healthIssueBeanFetch = root.fetch("healthIssueBeans", JoinType.LEFT);
        Fetch<HealthIssueBean, AdminBean> adminBeanFetch = healthIssueBeanFetch.fetch("adminBean", JoinType.LEFT);
        Fetch<HealthIssueBean, DiseaseBean> diseaseBeanFetch = healthIssueBeanFetch.fetch("diseaseBean", JoinType.LEFT);
        Fetch<CommunityUserBean, AdminBean> blockedByAdminBeanFetch = root.fetch("blockedByAdminBean", JoinType.LEFT);
    }

    private CommunityUserBean getResult(CriteriaQuery<CommunityUserBean> cq, Root<CommunityUserBean> root, @Nullable List<Predicate> predicates) {
        predicates = CollectionUtils.isEmpty(predicates) ? new ArrayList<>() : predicates;
        cq.select(root).distinct(true).where(predicates.toArray(new Predicate[]{}));
        Query<CommunityUserBean> query = currentSession().createQuery(cq);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private List<CommunityUserBean> getResults(CriteriaQuery<CommunityUserBean> cq, Root<CommunityUserBean> root, @Nullable List<Predicate> predicates) {
        predicates = CollectionUtils.isEmpty(predicates) ? new ArrayList<>() : predicates;
        cq.select(root).distinct(true).where(predicates.toArray(new Predicate[]{}));
        Query<CommunityUserBean> query = currentSession().createQuery(cq);
        return query.getResultList();
    }
}
