package com.chis.communityhealthis.bean;

import org.javers.core.metamodel.annotation.TypeName;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ADMIN")
@TypeName("AdminBean")
public class AdminBean implements Serializable {

    private static final long serialVersionUID = 2614607182089444001L;

    public static final String USERNAME = "USERNAME";
    public static final String FULL_NAME = "FULL_NAME";
    public static final String CONTACT_NO = "CONTACT_NO";
    public static final String PROFILE_PIC_DIRECTORY = "PROFILE_PIC_DIRECTORY";

    @Id
    @Column(name = USERNAME)
    private String username;

    @Column(name = FULL_NAME)
    private String fullName;

    @Column(name = CONTACT_NO)
    private String contactNo;

    @Column(name = PROFILE_PIC_DIRECTORY)
    private String profilePicDirectory;

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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getProfilePicDirectory() {
        return profilePicDirectory;
    }

    public void setProfilePicDirectory(String profilePicDirectory) {
        this.profilePicDirectory = profilePicDirectory;
    }
}
