package com.chis.communityhealthis.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reset_password_request")
public class ResetPasswordRequestBean implements Serializable {

    private static final long serialVersionUID = -2956748430669933386L;

    public final static String USERNAME = "USERNAME";
    public final static String OTP = "OTP";
    public final static String OTP_EXPIRY_DATE = "OTP_EXPIRY_DATE";

    @Id
    @Column(name = USERNAME)
    private String username;

    @Column(name = OTP)
    private String otp;

    @Column(name = OTP_EXPIRY_DATE)
    private Date otpExpiryDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getOtpExpiryDate() {
        return otpExpiryDate;
    }

    public void setOtpExpiryDate(Date otpExpiryDate) {
        this.otpExpiryDate = otpExpiryDate;
    }
}
