package com.chis.communityhealthis.model.user;

import com.chis.communityhealthis.model.role.RoleModel;

import java.util.Collection;

public class AdminDetailModel implements Comparable<AdminDetailModel>{

    private String username;
    private String fullName;
    private String email;
    private String contactNo;
    private String profilePicDirectory;
    private Boolean isDeletable;
    private Collection<RoleModel> roleList;

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

    public Boolean getDeletable() {return isDeletable;}

    public String getProfilePicDirectory() {
        return profilePicDirectory;
    }

    public void setProfilePicDirectory(String profilePicDirectory) {
        this.profilePicDirectory = profilePicDirectory;
    }

    public void setDeletable(Boolean deletable) {isDeletable = deletable;}

    public Collection<RoleModel> getRoleList() {
        return roleList;
    }

    public void setRoleList(Collection<RoleModel> roleList) {
        this.roleList = roleList;
    }

    @Override
    public int compareTo(AdminDetailModel o) {
        return getFullName().compareTo(o.getFullName());
    }
}
