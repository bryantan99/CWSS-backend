package com.chis.communityhealthis.repository.assistance;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.model.assistance.AssistanceQueryForm;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AssistanceDaoImpl extends GenericDaoImpl<AssistanceBean, Integer> implements AssistanceDao {

    @Override
    public List<AssistanceBean> findUserAssistanceRecords(AssistanceQueryForm form) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AssistanceBean> criteriaQuery = criteriaBuilder.createQuery(AssistanceBean.class);
        Root<AssistanceBean> root = criteriaQuery.from(AssistanceBean.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("username"), form.getUsername()));

        if (form.getAssistanceId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("assistanceId"), form.getAssistanceId()));
        }

        if (StringUtils.isNotBlank(form.getTitle())) {
            predicates.add(criteriaBuilder.like(root.get("assistanceTitle"), "%" + form.getTitle() + "%"));
        }

        if (StringUtils.isNotBlank(form.getStatus())) {
            predicates.add(criteriaBuilder.equal(root.get("assistanceStatus"), form.getStatus()));
        }

        fetchTables(root);
        return getResults(criteriaQuery, root, predicates);
    }

    @Override
    public List<AssistanceBean> findPendingAssistanceRecords(Integer assistanceId) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AssistanceBean> criteriaQuery = criteriaBuilder.createQuery(AssistanceBean.class);
        Root<AssistanceBean> root = criteriaQuery.from(AssistanceBean.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("assistanceStatus"), AssistanceBean.STATUS_PENDING));
        predicates.add(criteriaBuilder.isNull(root.get("adminUsername")));
        if (assistanceId != null) {
            predicates.add(criteriaBuilder.equal(root.get("assistanceId"), assistanceId));
        }

        fetchTables(root);
        return getResults(criteriaQuery, root, predicates);
    }

    @Override
    public List<AssistanceBean> findAdminHandledAssistanceRecords(AssistanceQueryForm queryForm) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AssistanceBean> criteriaQuery = criteriaBuilder.createQuery(AssistanceBean.class);
        Root<AssistanceBean> root = criteriaQuery.from(AssistanceBean.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("adminUsername"), queryForm.getAdminUsername()));

        if (queryForm.getAssistanceId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("assistanceId"), queryForm.getAssistanceId()));
        }

        if (StringUtils.isNotBlank(queryForm.getStatus())) {
            predicates.add(criteriaBuilder.equal(root.get("assistanceStatus"), queryForm.getStatus()));
        }

        if (StringUtils.isNotBlank(queryForm.getTitle())) {
            predicates.add(criteriaBuilder.like(root.get("assistanceTitle"), "%" + queryForm.getTitle() + "%"));
        }

        if (StringUtils.isNotBlank(queryForm.getUsername())) {
            predicates.add(criteriaBuilder.equal(root.get("username"), queryForm.getUsername()));
        }

        fetchTables(root);
        return getResults(criteriaQuery, root, predicates);
    }

    @Override
    public List<AssistanceBean> findAllAssistanceRecords(AssistanceQueryForm form) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AssistanceBean> criteriaQuery = criteriaBuilder.createQuery(AssistanceBean.class);
        Root<AssistanceBean> root = criteriaQuery.from(AssistanceBean.class);

        List<Predicate> predicates = new ArrayList<>();

        if (form.getAssistanceId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("assistanceId"), form.getAssistanceId()));
        }

        if (form.getCategoryId() != null) {
            if (form.getCategoryId().equals(AssistanceBean.CATEGORY_ID_NULL_CODE)) {
                predicates.add(criteriaBuilder.isNull(root.get("categoryId")));
            } else {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), form.getCategoryId()));
            }
        }

        if (StringUtils.isNotBlank(form.getTitle())) {
            predicates.add(criteriaBuilder.like(root.get("assistanceTitle"), "%" + form.getTitle() + "%"));
        }

        if (StringUtils.isNotBlank(form.getStatus())) {
            predicates.add(criteriaBuilder.equal(root.get("assistanceStatus"), form.getStatus()));
        }

        if (StringUtils.isNotBlank(form.getNric())) {
            predicates.add(criteriaBuilder.like(root.get("username"), "%" + form.getNric() + "%"));
        }

        if (StringUtils.isNotBlank(form.getAdminUsername())) {
            predicates.add(criteriaBuilder.equal(root.get("adminUsername"), form.getAdminUsername()));
        }

        fetchTables(root);
        return getResults(criteriaQuery, root, predicates);
    }

    private void fetchTables(Root<AssistanceBean> root) {
        Fetch<AssistanceBean, CommunityUserBean> communityUserBeanFetch = root.fetch("communityUserBean", JoinType.LEFT);
        Fetch<CommunityUserBean, AddressBean> addressBeanFetch = communityUserBeanFetch.fetch("addressBean", JoinType.LEFT);
        Fetch<AddressBean, ZoneBean> zoneBeanFetch = addressBeanFetch.fetch("zoneBean", JoinType.LEFT);
        Fetch<CommunityUserBean, OccupationBean> occupationBeanFetch = communityUserBeanFetch.fetch("occupationBean", JoinType.LEFT);
        Fetch<AssistanceBean, AdminBean> adminBeanFetch = root.fetch("adminBean", JoinType.LEFT);
        Fetch<AssistanceBean, AssistanceCategoryBean> categoryBeanFetch = root.fetch("categoryBean", JoinType.LEFT);
        Fetch<AssistanceBean, AppointmentBean> appointmentBeanFetch = root.fetch("appointmentBean", JoinType.LEFT);
    }

    private List<AssistanceBean> getResults(CriteriaQuery<AssistanceBean> cq, Root<AssistanceBean> root, List<Predicate> predicates) {
        cq.select(root).distinct(true).where(predicates.toArray(new Predicate[]{}));
        Query<AssistanceBean> query = currentSession().createQuery(cq);
        return query.list();
    }
}
