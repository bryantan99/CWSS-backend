package com.chis.communityhealthis.model.jwt;

import com.google.gson.Gson;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;

public class JwtResponseModel implements Serializable {

    private static final long serialVersionUID = 8256229639433822362L;
    private final String username;
    private final List<String> roleList;
    private final String refreshToken;
    private final String jwtToken;
    private final Long expirationDate;

    public JwtResponseModel(UserDetails userDetails, String jwtToken, String refreshToken) {
        this.username = userDetails.getUsername();
        this.roleList = extractRoleList(userDetails.getAuthorities());
        this.refreshToken = refreshToken;
        this.jwtToken = jwtToken;
        this.expirationDate = decodeJwtTokenToGetExpirationDate();
    }

    private Long decodeJwtTokenToGetExpirationDate() {
        String[] chunks = jwtToken.split("\\.");
        if (chunks.length > 0) {
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payloadJsonString = new String(decoder.decode(chunks[1]));
            Gson g = new Gson();
            PayloadModel payloadModel = g.fromJson(payloadJsonString, PayloadModel.class);
            return payloadModel.getExpirationDate();
        }
        return null;
    }

    private List<String> extractRoleList(Collection<? extends GrantedAuthority> authorities) {
        List<String> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(authorities)) {
            authorities.forEach(e -> list.add(e.getAuthority()));
        }
        return list;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getUsername() { return username; }

    public List<String> getRoleList() { return roleList; }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
