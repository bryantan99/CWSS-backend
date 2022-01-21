package com.chis.communityhealthis.model.user;

import com.chis.communityhealthis.model.address.AddressModel;
import com.chis.communityhealthis.model.health.HealthModel;

import java.util.Date;
import java.util.List;

public class CommunityUserProfileModel implements Comparable<CommunityUserProfileModel> {
    private String username;
    private String accIsActivate;
    private String blockedBy;
    private String blockedByFullName;
    private Date blockedDate;
    private String blockedMessage;
    private String email;
    private PersonalDetailModel personalDetail;
    private AddressModel address;
    private List<HealthModel> healthModelList;

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

    public String getAccIsActivate() { return accIsActivate; }

    public void setAccIsActivate(String accIsActivate) { this.accIsActivate = accIsActivate; }

    public PersonalDetailModel getPersonalDetail() {
        return personalDetail;
    }

    public void setPersonalDetail(PersonalDetailModel personalDetail) {
        this.personalDetail = personalDetail;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public List<HealthModel> getHealthModelList() { return healthModelList; }

    public void setHealthModelList(List<HealthModel> healthModelList) { this.healthModelList = healthModelList; }

    public String getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(String blockedBy) {
        this.blockedBy = blockedBy;
    }

    public String getBlockedByFullName() {
        return blockedByFullName;
    }

    public void setBlockedByFullName(String blockedByFullName) {
        this.blockedByFullName = blockedByFullName;
    }

    public Date getBlockedDate() {
        return blockedDate;
    }

    public void setBlockedDate(Date blockedDate) {
        this.blockedDate = blockedDate;
    }

    public String getBlockedMessage() {
        return blockedMessage;
    }

    public void setBlockedMessage(String blockedMessage) {
        this.blockedMessage = blockedMessage;
    }

    @Override
    public int compareTo(CommunityUserProfileModel o) {
        return this.personalDetail.getFullName().compareTo(o.getPersonalDetail().getFullName());
    }
}
