package com.chis.communityhealthis.model.filter;

public class CommunityUserBeanJoinFilter {
    private boolean includeAddress;
    private boolean includeOccupation;
    private boolean includeHealthIssue;

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
