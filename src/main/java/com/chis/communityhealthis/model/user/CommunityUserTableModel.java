package com.chis.communityhealthis.model.user;

public class CommunityUserTableModel implements Comparable<CommunityUserTableModel> {
    private String username;
    private String fullName;
    private String nricNo;
    private String email;
    private String isActive;

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

    public String getNricNo() { return nricNo; }

    public void setNricNo(String nricNo) { this.nricNo = nricNo; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public int compareTo(CommunityUserTableModel o) {
        return getFullName().compareTo(o.getFullName());
    }
}
