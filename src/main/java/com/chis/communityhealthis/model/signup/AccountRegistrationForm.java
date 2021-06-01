package com.chis.communityhealthis.model.signup;

public class AccountRegistrationForm {
    private PersonalDetailForm personalDetail;
    private AddressForm address;
    private OccupationForm occupation;

    public PersonalDetailForm getPersonalDetail() {
        return personalDetail;
    }

    public void setPersonalDetail(PersonalDetailForm personalDetail) {
        this.personalDetail = personalDetail;
    }

    public AddressForm getAddress() {
        return address;
    }

    public void setAddress(AddressForm address) {
        this.address = address;
    }

    public OccupationForm getOccupation() {
        return occupation;
    }

    public void setOccupation(OccupationForm occupation) {
        this.occupation = occupation;
    }
}
