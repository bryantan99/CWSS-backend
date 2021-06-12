package com.chis.communityhealthis.model.signup;

import java.util.List;

public class HealthForm {
    private List<HealthIssueModel> diseaseList;

    public List<HealthIssueModel> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(List<HealthIssueModel> diseaseList) {
        this.diseaseList = diseaseList;
    }
}
