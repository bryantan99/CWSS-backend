package com.chis.communityhealthis.repository.refreshtoken;

import com.chis.communityhealthis.bean.RefreshTokenBean;
import com.chis.communityhealthis.repository.GenericDao;

public interface RefreshTokenDao extends GenericDao<RefreshTokenBean, Long> {
    RefreshTokenBean findByToken(String token);
    RefreshTokenBean findByUsername(String username);
    int deleteByUsername(String username);
}
