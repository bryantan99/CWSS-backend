package com.chis.communityhealthis.model.user;

import com.chis.communityhealthis.model.address.AddressModel;
import com.chis.communityhealthis.model.health.HealthModel;

import java.util.List;

public class CommunityUserModel implements Comparable<CommunityUserModel> {
    private String username;
    private String fullName;
    private String gender;
    private String ethnic;
    private String contactNo;
    private String nric;
    private AddressModel address;
    private List<HealthModel> healthIssues;
    private String accIsActivate;
    private String accIsBlocked;
    private String email;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public List<HealthModel> getHealthIssues() {
        return healthIssues;
    }

    public void setHealthIssues(List<HealthModel> healthIssues) {
        this.healthIssues = healthIssues;
    }

    public String getAccIsActivate() {
        return accIsActivate;
    }

    public void setAccIsActivate(String accIsActivate) {
        this.accIsActivate = accIsActivate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccIsBlocked() {
        return accIsBlocked;
    }

    public void setAccIsBlocked(String accIsBlocked) {
        this.accIsBlocked = accIsBlocked;
    }

    @Override
    public int compareTo(CommunityUserModel o) {
        return getFullName().compareTo(o.getFullName());
    }
}
