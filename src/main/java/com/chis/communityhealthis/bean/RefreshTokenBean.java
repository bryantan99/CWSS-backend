package com.chis.communityhealthis.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = "refresh_token")
@Table(name = "refresh_token")
public class RefreshTokenBean implements Serializable {

    private static final long serialVersionUID = 946273171213005100L;

    public static final String REFRESH_TOKEN_ID = "REFRESH_TOKEN_ID";
    public static final String USERNAME = "USERNAME";
    public static final String TOKEN = "TOKEN";
    public static final String EXPIRY_DATE = "EXPIRY_DATE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = REFRESH_TOKEN_ID)
    private Long refreshTokenId;

    @Column(name = USERNAME)
    private String username;

    @Column(name = TOKEN)
    private String token;

    @Column(name = EXPIRY_DATE)
    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = USERNAME, referencedColumnName = AccountBean.USERNAME, insertable = false, updatable = false)
    private AccountBean accountBean;

    public RefreshTokenBean() {
    }

    public long getRefreshTokenId() {
        return refreshTokenId;
    }

    public void setRefreshTokenId(long refreshTokenId) {
        this.refreshTokenId = refreshTokenId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public AccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
    }
}
