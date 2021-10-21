package com.chis.communityhealthis.model.user;

import com.chis.communityhealthis.model.address.AddressModel;
import com.chis.communityhealthis.model.health.HealthModel;
import com.chis.communityhealthis.model.occupation.OccupationModel;

import java.util.List;

public class CommunityUserProfileModel implements Comparable<CommunityUserProfileModel> {
    private String username;
    private String accIsActivate;
    private String email;
    private PersonalDetailModel personalDetail;
    private AddressModel address;
    private OccupationModel occupation;
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

    public OccupationModel getOccupation() {
        return occupation;
    }

    public void setOccupation(OccupationModel occupation) {
        this.occupation = occupation;
    }

    public List<HealthModel> getHealthModelList() { return healthModelList; }

    public void setHealthModelList(List<HealthModel> healthModelList) { this.healthModelList = healthModelList; }

    @Override
    public int compareTo(CommunityUserProfileModel o) {
        return this.personalDetail.getFullName().compareTo(o.getPersonalDetail().getFullName());
    }
}
