package com.chis.communityhealthis.model.occupation;

public class OccupationModel {
    private String employmentStatus;
    private String occupationName;
    private Double salary;
    private String employerCompany;
    private String employerContactNo;

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getEmployerCompany() {
        return employerCompany;
    }

    public void setEmployerCompany(String employerCompany) {
        this.employerCompany = employerCompany;
    }

    public String getEmployerContactNo() {
        return employerContactNo;
    }

    public void setEmployerContactNo(String employerContactNo) {
        this.employerContactNo = employerContactNo;
    }
}
