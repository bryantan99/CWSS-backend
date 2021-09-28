package com.chis.communityhealthis.model.jwt;

import com.chis.communityhealthis.bean.AccountRoleBean;
import com.chis.communityhealthis.security.UserDetailsModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtResponseModel implements Serializable {

    private static final long serialVersionUID = 8256229639433822362L;
    private final String username;
    private final List<String> roleList;
    private final String jwtToken;

    public JwtResponseModel(UserDetails userDetails, String jwtToken) {
        this.username = userDetails.getUsername();
        this.roleList = extractRoleList(userDetails.getAuthorities());
        this.jwtToken = jwtToken;
    }

    private List<String> extractRoleList(Collection<? extends GrantedAuthority> authorities) {
        List<String> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(authorities)) {
            authorities.forEach(e -> {
                list.add(e.getAuthority());
            });
        }
        return list;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getUsername() { return username; }

    public List<String> getRoleList() { return roleList; }
}
