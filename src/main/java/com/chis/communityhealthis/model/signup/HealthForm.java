package com.chis.communityhealthis.model.signup;

import java.util.List;

public class HealthForm {
    private List<HealthDiseaseModel> diseaseList;

    public List<HealthDiseaseModel> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(List<HealthDiseaseModel> diseaseList) {
        this.diseaseList = diseaseList;
    }
}
