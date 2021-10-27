package com.chis.communityhealthis.repository.zone;

import com.chis.communityhealthis.bean.ZoneBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.Arrays;
import java.util.List;

@Repository
public class ZoneDaoImpl extends GenericDaoImpl<ZoneBean, Integer> implements ZoneDao {

    @Override
    public List<ZoneBean> getZoneDropdownChoices() {
        CriteriaBuilder cb = currentSession().getCriteriaBuilder();
        CriteriaQuery<ZoneBean> cq = cb.createQuery(ZoneBean.class);
        Root<ZoneBean> root = cq.from(ZoneBean.class);
        List<Selection<?>> columns = Arrays.asList(root.get("zoneId"), root.get("zoneName"));
        cq.multiselect(columns);
        Query<ZoneBean> query = currentSession().createQuery(cq);
        return query.list();
    }
}
