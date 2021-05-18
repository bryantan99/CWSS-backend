package com.chis.communityhealthis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "account")
public class AccountBean implements Serializable {

    private static final long serialVersionUID = -366641835277597847L;

    public final static String ACCOUNT_USERNAME = "ACCOUNT_USERNAME";
    public final static String ACCOUNT_PASSWORD = "ACCOUNT_PASSWORD";
    public final static String ACCOUNT_STATUS = "ACCOUNT_STATUS";
    public final static String LAST_LOGIN_DATE = "LAST_LOGIN_DATE";

    @Id
    @Column(name = ACCOUNT_USERNAME)
    private String accountUsername;

    @Column(name = ACCOUNT_PASSWORD)
    private String accountPassword;

    @Column(name = ACCOUNT_STATUS)
    private String accountStatus;

    @Column(name = LAST_LOGIN_DATE)
    private Date lastLoginDate;

    public AccountBean() {
    }

    public String getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(String username) {
        this.accountUsername = username;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String password) {
        this.accountPassword = password;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String status) {
        this.accountStatus = status;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLogin) {
        this.lastLoginDate = lastLogin;
    }

    @Override
    public String toString() {
        return "AccountBean{" +
                "accountUsername='" + accountUsername + '\'' +
                ", accountPassword='" + accountPassword + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", lastLoginDate=" + lastLoginDate +
                '}';
    }
}
