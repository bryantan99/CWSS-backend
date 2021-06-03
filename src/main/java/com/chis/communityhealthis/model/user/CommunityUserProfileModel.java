package com.chis.communityhealthis.model.user;

import com.chis.communityhealthis.bean.AddressBean;
import com.chis.communityhealthis.bean.CommunityUserBean;
import com.chis.communityhealthis.bean.OccupationBean;

public class CommunityUserProfileModel {
    private String accIsActivate;
    private CommunityUserBean personalDetail;
    private AddressBean address;
    private OccupationBean occupation;

    public String getAccIsActivate() { return accIsActivate; }

    public void setAccIsActivate(String accIsActivate) { this.accIsActivate = accIsActivate; }

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
}
