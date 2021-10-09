package com.chis.communityhealthis.repository.occupation;

import com.chis.communityhealthis.bean.OccupationBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface OccupationDao extends GenericDao<OccupationBean, String> {
    List<OccupationBean> getUsersOccupation(List<String> usernameList);
}
