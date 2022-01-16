package com.chis.communityhealthis.utility;

import com.chis.communityhealthis.security.UserDetailsModel;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateJwtToken(UserDetailsModel userPrincipal) {
        return prepareClaimAndGenerateToken(userPrincipal);
    }

    public String generateToken(UserDetails userDetails) {
        return prepareClaimAndGenerateToken(userDetails);
    }

    private String prepareClaimAndGenerateToken(UserDetails userDetails) {
        List<String> roleList = prepareRoleListClaim(userDetails.getAuthorities());
        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationMs);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roleList", roleList);
        claims.put("expirationDate", expirationDate);
        return doGenerateToken(claims, userDetails.getUsername(), expirationDate);
    }

    private List<String> prepareRoleListClaim(Collection<? extends GrantedAuthority> authorities) {
        List<String> roleList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(authorities)) {
            for (GrantedAuthority authority : authorities) {
                roleList.add(authority.getAuthority());
            }
        }
        return roleList;
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, Date expirationDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
