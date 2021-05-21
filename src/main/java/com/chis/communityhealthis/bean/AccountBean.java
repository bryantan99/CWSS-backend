package com.chis.communityhealthis.bean;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "account")
@Table(name = "account")
public class AccountBean implements Serializable {

    private static final long serialVersionUID = -366641835277597847L;

    public final static String USERNAME = "USERNAME";
    public final static String PW = "PW";
    public final static String IS_ACTIVE = "IS_ACTIVE";
    public final static String LAST_LOGIN_DATE = "LAST_LOGIN_DATE";

    @Id
    @Column(name = USERNAME)
    private String username;

    @Column(name = PW)
    private String pw;

    @Column(name = IS_ACTIVE)
    private String isActive;

    @Column(name = LAST_LOGIN_DATE)
    private Date lastLoginDate;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = AccountRoleBean.USERNAME, referencedColumnName = USERNAME)
    @JsonManagedReference
    private Set<AccountRoleBean> roles = new HashSet<>();

    public AccountBean() {
    }

    public AccountBean(String username, String pw, String isActive, Date lastLoginDate) {
        this.username = username;
        this.pw = pw;
        this.isActive = isActive;
        this.lastLoginDate = lastLoginDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String password) {
        this.pw = password;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String status) {
        this.isActive = status;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLogin) {
        this.lastLoginDate = lastLogin;
    }

    public Set<AccountRoleBean> getRoles() { return roles; }

    public void setRoles(Set<AccountRoleBean> accountRoleBeanList) { this.roles = accountRoleBeanList; }
}
