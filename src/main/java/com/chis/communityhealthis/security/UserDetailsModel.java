package com.chis.communityhealthis.security;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.bean.AccountRoleBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsModel implements UserDetails {

    private String username;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;

    public UserDetailsModel(AccountBean accountBean) {
        this.username = accountBean.getUsername();
        this.password = accountBean.getPw();
        this.active = "Y".equals(accountBean.getIsActive());
        this.authorities = extractRoles(accountBean.getRoles());
    }

    private List<GrantedAuthority> extractRoles(Collection<AccountRoleBean> accountRoleBeanList) {
        List<GrantedAuthority> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(accountRoleBeanList)) {
            for (AccountRoleBean role : accountRoleBeanList) {
                list.add(new SimpleGrantedAuthority(role.getRoleName()));
            }
        }
        return list;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
