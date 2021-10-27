package com.chis.communityhealthis.repository.zone;

import com.chis.communityhealthis.bean.ZoneBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface ZoneDao extends GenericDao<ZoneBean, Integer> {
    List<ZoneBean> getZoneDropdownChoices();
}
