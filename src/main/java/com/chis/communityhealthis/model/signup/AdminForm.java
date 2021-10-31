package com.chis.communityhealthis.model.signup;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class AdminForm {
    private String fullName;
    private String username;
    private String email;
    private String contactNo;
    private List<String> roleList;
    private String password;
    private String createdBy;
    private MultipartFile profilePicFile;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public MultipartFile getProfilePicFile() {
        return profilePicFile;
    }

    public void setProfilePicFile(MultipartFile profilePicFile) {
        this.profilePicFile = profilePicFile;
    }
}
