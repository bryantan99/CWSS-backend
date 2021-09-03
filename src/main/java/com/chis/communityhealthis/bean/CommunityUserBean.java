package com.chis.communityhealthis.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "COMMUNITY_USER")
public class CommunityUserBean implements Serializable {

    private static final long serialVersionUID = -1198909767225283237L;

    public static final String USERNAME = "USERNAME";
    public static final String FULL_NAME = "FULL_NAME";
    public static final String NRIC = "NRIC";
    public static final String GENDER = "GENDER";
    public static final String CONTACT_NO = "CONTACT_NO";
    public static final String ETHNIC = "ETHNIC";

    @Id
    @Column(name = USERNAME)
    private String username;

    @Column(name = FULL_NAME)
    private String fullName;

    @Column(name = NRIC)
    private String nric;

    @Column(name = GENDER)
    private String gender;

    @Column(name = CONTACT_NO)
    private String contactNo;

    @Column(name = ETHNIC)
    private String ethnic;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }
}
