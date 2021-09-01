package com.chis.communityhealthis.bean;

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
    @Column(name = ID, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = USERNAME)
    private String username;

    @Column(name = ROLE_NAME)
    private String roleName;

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
}
