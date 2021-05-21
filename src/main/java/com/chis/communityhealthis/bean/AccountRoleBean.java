package com.chis.communityhealthis.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "account_role")
@Table(name = "account_role")
public class AccountRoleBean implements Serializable {

    private static final long serialVersionUID = 7227031577810342699L;

    public static final String ID = "ID";
    public static final String USERNAME = "USERNAME";
    public static final String ROLE_NAME = "ROLE_NAME";

    @Id
    @Column(name = ID)
    private Integer id;

    @Column(name = USERNAME)
    private String username;

    @Column(name = ROLE_NAME)
    private String roleName;

    @ManyToOne
    @JoinColumn(name = AccountBean.USERNAME, referencedColumnName = USERNAME, insertable = false, updatable = false)
    @JsonBackReference
    private AccountBean accountBean;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public AccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
    }
}
