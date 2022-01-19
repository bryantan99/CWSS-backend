package com.chis.communityhealthis.service.refreshtoken;

import com.chis.communityhealthis.bean.RefreshTokenBean;

public interface RefreshTokenService {
    RefreshTokenBean getByToken(String token);
    RefreshTokenBean createRefreshToken(String username);
    RefreshTokenBean verifyExpiration(RefreshTokenBean refreshTokenBean);
    void deleteByUsername(String username);
}
