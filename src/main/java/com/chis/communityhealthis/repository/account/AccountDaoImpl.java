package com.chis.communityhealthis.repository.account;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public class AccountDaoImpl extends GenericDaoImpl<AccountBean, String> implements AccountDao {

    //  AccountBean's variable name in AccountBean.class
    public static final String VAR_USERNAME = "username";
    public static final String VAR_PASSWORD = "pw";
    public static final String VAR_IS_ACTIVE = "isActive";
    public static final String VAR_LAST_LOGIN_DATE = "lastLoginDate";
    public static final String VAR_EMAIL = "email";

    public static final String JOIN_VAR_ROLES = "roles";

    @Override
    public AccountBean findAccount(String username) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AccountBean> criteriaQuery = criteriaBuilder.createQuery(AccountBean.class);
        Root<AccountBean> root = criteriaQuery.from(AccountBean.class);

        List<Selection<?>> columns = Arrays.asList(root.get(VAR_USERNAME), root.get(VAR_PASSWORD), root.get(VAR_IS_ACTIVE), root.get(VAR_LAST_LOGIN_DATE), root.get(VAR_EMAIL));
        criteriaQuery.multiselect(columns).where(criteriaBuilder.equal(root.get("username"), username));

        Query<AccountBean> query = currentSession().createQuery(criteriaQuery);
        return query.uniqueResult();
    }

    @Override
    public List<AccountBean> findAccounts(Collection<String> usernames) {
        if (CollectionUtils.isEmpty(usernames)) {
            return new ArrayList<>();
        }

        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AccountBean> criteriaQuery = criteriaBuilder.createQuery(AccountBean.class);
        Root<AccountBean> root = criteriaQuery.from(AccountBean.class);

        List<Selection<?>> columns = Arrays.asList(root.get(VAR_USERNAME), root.get(VAR_PASSWORD), root.get(VAR_IS_ACTIVE), root.get(VAR_LAST_LOGIN_DATE), root.get(VAR_EMAIL));
        CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get(VAR_USERNAME));

        for (String username : usernames) {
            inClause.value(username);
        }

        criteriaQuery.multiselect(columns)
                .where(inClause);

        Query<AccountBean> query = currentSession().createQuery(criteriaQuery);
        return query.list();
    }

    @Override
    public AccountBean findAccountWithRoles(String username) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AccountBean> criteriaQuery = criteriaBuilder.createQuery(AccountBean.class);
        Root<AccountBean> root = criteriaQuery.from(AccountBean.class);
        root.fetch(JOIN_VAR_ROLES, JoinType.LEFT);
        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get(VAR_USERNAME), username));

        Query<AccountBean> query = currentSession().createQuery(criteriaQuery);
        return query.uniqueResult();
    }

    @Override
    public AccountBean findAccountByEmail(String email) {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AccountBean> criteriaQuery = criteriaBuilder.createQuery(AccountBean.class);
        Root<AccountBean> root = criteriaQuery.from(AccountBean.class);
        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get(VAR_EMAIL), email));

        Query<AccountBean> query = currentSession().createQuery(criteriaQuery);
        return query.uniqueResult();
    }
}
