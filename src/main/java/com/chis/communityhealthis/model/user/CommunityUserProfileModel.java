package com.chis.communityhealthis.model.user;

import com.chis.communityhealthis.bean.AddressBean;
import com.chis.communityhealthis.bean.CommunityUserBean;
import com.chis.communityhealthis.bean.OccupationBean;
import com.chis.communityhealthis.model.health.HealthModel;

import java.util.List;

public class CommunityUserProfileModel {
    private String accIsActivate;
    private String email;
    private CommunityUserBean personalDetail;
    private AddressBean address;
    private OccupationBean occupation;
    private List<HealthModel> healthModelList;

    public String getAccIsActivate() { return accIsActivate; }

    public void setAccIsActivate(String accIsActivate) { this.accIsActivate = accIsActivate; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CommunityUserBean getPersonalDetail() {
        return personalDetail;
    }

    public void setPersonalDetail(CommunityUserBean personalDetail) {
        this.personalDetail = personalDetail;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public OccupationBean getOccupation() {
        return occupation;
    }

    public void setOccupation(OccupationBean occupation) {
        this.occupation = occupation;
    }

    public List<HealthModel> getHealthModelList() { return healthModelList; }

    public void setHealthModelList(List<HealthModel> healthModelList) { this.healthModelList = healthModelList; }
}
