package com.chis.communityhealthis.service.refreshtoken;

import com.chis.communityhealthis.bean.RefreshTokenBean;
import com.chis.communityhealthis.exception.TokenRefreshException;
import com.chis.communityhealthis.repository.refreshtoken.RefreshTokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh.expiration.ms}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenDao refreshTokenDao;

    @Override
    @Transactional
    public RefreshTokenBean getByToken(String token) {
        return refreshTokenDao.findByToken(token);
    }

    @Override
    @Transactional
    public RefreshTokenBean createRefreshToken(String username) {
        RefreshTokenBean refreshToken = refreshTokenDao.findByUsername(username);
        if (refreshToken == null) {
            refreshToken = new RefreshTokenBean();
            refreshToken.setUsername(username);
        }
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenDao.saveOrUpdate(refreshToken);
        return refreshToken;
    }

    @Override
    @Transactional(noRollbackFor = TokenRefreshException.class)
    public RefreshTokenBean verifyExpiration(RefreshTokenBean token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenDao.remove(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign in request");
        }

        return token;
    }
}
