package com.chis.communityhealthis.model.user;

public class AdminDetailModel implements Comparable<AdminDetailModel>{

    private String username;
    private String fullName;
    private String email;
    private String contactNo;
    private Boolean isDeletable;

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

    public void setDeletable(Boolean deletable) {isDeletable = deletable;}

    @Override
    public int compareTo(AdminDetailModel o) {
        return getFullName().compareTo(o.getFullName());
    }
}
