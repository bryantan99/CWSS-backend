package com.chis.communityhealthis.repository.accountrole;

import com.chis.communityhealthis.bean.AccountRoleBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class AccountRoleDaoImpl extends GenericDaoImpl<AccountRoleBean, Integer> implements AccountRoleDao {
    @Override
    public List<AccountRoleBean> findUserRoles(String username) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AccountRoleBean> criteriaQuery = criteriaBuilder.createQuery(AccountRoleBean.class);
        Root<AccountRoleBean> root = criteriaQuery.from(AccountRoleBean.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("username"), username));
        Query<AccountRoleBean> query = currentSession().createQuery(criteriaQuery);

        return query.list();
    }
}
