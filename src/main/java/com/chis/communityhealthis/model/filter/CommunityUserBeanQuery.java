package com.chis.communityhealthis.model.filter;

public class CommunityUserBeanQuery {
    private String name;
    private String nric;
    private String gender;
    private String ethnic;
    private boolean includeAddress;
    private boolean includeOccupation;
    private boolean includeHealthIssue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
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

    public boolean isIncludeAddress() {
        return includeAddress;
    }

    public void setIncludeAddress(boolean includeAddress) {
        this.includeAddress = includeAddress;
    }

    public boolean isIncludeOccupation() {
        return includeOccupation;
    }

    public void setIncludeOccupation(boolean includeOccupation) {
        this.includeOccupation = includeOccupation;
    }

    public boolean isIncludeHealthIssue() {
        return includeHealthIssue;
    }

    public void setIncludeHealthIssue(boolean includeHealthIssue) {
        this.includeHealthIssue = includeHealthIssue;
    }
}
