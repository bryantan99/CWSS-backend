package com.chis.communityhealthis.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DISEASE")
public class DiseaseBean implements Serializable {

    private static final long serialVersionUID = 3949907929811261386L;

    public static final String DISEASE_ID = "DISEASE_ID";
    public static final String DISEASE_NAME = "DISEASE_NAME";

    @Id
    @Column(name = DISEASE_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer diseaseId;

    @Column(name = DISEASE_NAME)
    private String diseaseName;

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }
}
