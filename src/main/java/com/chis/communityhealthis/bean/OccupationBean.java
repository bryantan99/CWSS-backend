package com.chis.communityhealthis.bean;

import org.javers.core.metamodel.annotation.TypeName;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "OCCUPATION")
@TypeName("OccupationBean")
public class OccupationBean implements Serializable {

    private static final long serialVersionUID = -6531027889635304041L;

    public static final String USERNAME = "USERNAME";
    public static final String EMPLOYMENT_TYPE = "EMPLOYMENT_TYPE";
    public static final String OCCUPATION_NAME = "OCCUPATION_NAME";
    public static final String SALARY = "SALARY";
    public static final String EMPLOYER_COMPANY = "EMPLOYER_COMPANY";
    public static final String EMPLOYER_CONTACT_NO = "EMPLOYER_CONTACT_NO";
    public static final String APPROVED_BY = "APPROVED_BY";
    public static final String APPROVED_DATE = "APPROVED_DATE";

    public static final String EMPLOYMENT_TYPE_UNEMPLOYED = "-";
    public static final String EMPLOYMENT_TYPE_SELF_UNEMPLOYED = "S/E";
    public static final String EMPLOYMENT_TYPE_GOVERNMENT = "GOVT";
    public static final String EMPLOYMENT_TYPE_PRIVATE = "PVT";

    @Id
    @Column(name = USERNAME)
    private String username;

    @Column(name = EMPLOYMENT_TYPE)
    private String employmentType;

    @Column(name = OCCUPATION_NAME)
    private String occupationName;

    @Column(name = SALARY)
    private Double salary;

    @Column(name = EMPLOYER_COMPANY)
    private String employerCompany;

    @Column(name = EMPLOYER_CONTACT_NO)
    private String employerContactNo;

    @Column(name = APPROVED_BY)
    private String approvedBy;

    @Column(name = APPROVED_DATE)
    private Date approvedDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String occupationType) {
        this.employmentType = occupationType;
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

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }
}
