package com.chis.communityhealthis.bean;

import org.javers.core.metamodel.annotation.TypeName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "account")
@Table(name = "account")
@TypeName("AccountBean")
public class AccountBean implements Serializable {

    private static final long serialVersionUID = -366641835277597847L;

    public final static String USERNAME = "USERNAME";
    public final static String PW = "PW";
    public final static String IS_ACTIVE = "IS_ACTIVE";
    public final static String LAST_LOGIN_DATE = "LAST_LOGIN_DATE";
    public final static String EMAIL = "EMAIL";

    @Id
    @Column(name = USERNAME)
    private String username;

    @Column(name = PW)
    private String pw;

    @Column(name = IS_ACTIVE)
    private String isActive;

    @Column(name = LAST_LOGIN_DATE)
    private Date lastLoginDate;

    @Column(name = EMAIL)
    private String email;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = USERNAME, referencedColumnName = AccountRoleBean.USERNAME, updatable = false, insertable = false)
    private Set<AccountRoleBean> roles = new HashSet<>();

    public AccountBean() {
    }

    public AccountBean(String username, String pw, String isActive, Date lastLoginDate, String email) {
        this.username = username;
        this.pw = pw;
        this.isActive = isActive;
        this.lastLoginDate = lastLoginDate;
        this.email = email;
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

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}
