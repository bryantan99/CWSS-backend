package com.chis.communityhealthis.repository.address;

import com.chis.communityhealthis.bean.AddressBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AddressDaoImpl extends GenericDaoImpl<AddressBean, String> implements AddressDao {

    @Override
    public List<AddressBean> getUsersAddress(List<String> usernameList) {
        if (CollectionUtils.isEmpty(usernameList)) {
            return new ArrayList<>();
        }

        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<AddressBean> criteriaQuery = criteriaBuilder.createQuery(AddressBean.class);
        Root<AddressBean> root = criteriaQuery.from(AddressBean.class);

        CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("username"));
        for (String username : usernameList) {
            inClause.value(username);
        }

        criteriaQuery.select(root).where(inClause);

        Query<AddressBean> query = currentSession().createQuery(criteriaQuery);
        return query.getResultList();
    }

}
