package com.chis.communityhealthis.model.email.template;

public class StaffAccountCreationEmailTemplateModel {
    private String fullName;
    private String username;
    private String generatedPw;

    public StaffAccountCreationEmailTemplateModel() {
    }

    public StaffAccountCreationEmailTemplateModel(String fullName, String username, String generatedPw) {
        this.fullName = fullName;
        this.username = username;
        this.generatedPw = generatedPw;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGeneratedPw() {
        return generatedPw;
    }

    public void setGeneratedPw(String generatedPw) {
        this.generatedPw = generatedPw;
    }
}
