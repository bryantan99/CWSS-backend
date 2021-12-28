package com.chis.communityhealthis.bean;

import org.javers.core.metamodel.annotation.TypeName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "COMMUNITY_USER")
@TypeName("CommunityUserBean")
public class CommunityUserBean implements Serializable {

    private static final long serialVersionUID = -1198909767225283237L;

    public static final String USERNAME = "USERNAME";
    public static final String FULL_NAME = "FULL_NAME";
    public static final String NRIC = "NRIC";
    public static final String GENDER = "GENDER";
    public static final String CONTACT_NO = "CONTACT_NO";
    public static final String ETHNIC = "ETHNIC";
    public static final String BLOCKED_BY = "BLOCKED_BY";
    public static final String BLOCKED_DATE = "BLOCKED_DATE";
    public static final String BLOCKED_MESSAGE = "BLOCKED_MESSAGE";

    @Id
    @Column(name = USERNAME)
    private String username;

    @Column(name = FULL_NAME)
    private String fullName;

    @Column(name = NRIC)
    private String nric;

    @Column(name = GENDER)
    private String gender;

    @Column(name = CONTACT_NO)
    private String contactNo;

    @Column(name = ETHNIC)
    private String ethnic;

    @Column(name = BLOCKED_BY)
    private String blockedBy;

    @Column(name = BLOCKED_DATE)
    private Date blockedDate;

    @Column(name = BLOCKED_MESSAGE)
    private String blockedMessage;

    @OneToOne
    @JoinColumn(name = BLOCKED_BY, referencedColumnName = AdminBean.USERNAME, insertable = false, updatable = false)
    private AdminBean blockedByAdminBean;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = USERNAME, referencedColumnName = AddressBean.USERNAME, insertable = false, updatable = false)
    private AddressBean addressBean;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = USERNAME, referencedColumnName = OccupationBean.USERNAME, insertable = false, updatable = false)
    private OccupationBean occupationBean;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = USERNAME, referencedColumnName = HealthIssueBean.USERNAME, insertable = false, updatable = false)
    private Set<HealthIssueBean> healthIssueBeans;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public Date getBlockedDate() {
        return blockedDate;
    }

    public void setBlockedDate(Date blockedDate) {
        this.blockedDate = blockedDate;
    }

    public String getBlockedMessage() {
        return blockedMessage;
    }

    public void setBlockedMessage(String blockedMessage) {
        this.blockedMessage = blockedMessage;
    }

    public String getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(String blockedBy) {
        this.blockedBy = blockedBy;
    }

    public AdminBean getBlockedByAdminBean() {
        return blockedByAdminBean;
    }

    public void setBlockedByAdminBean(AdminBean blockedByAdminBean) {
        this.blockedByAdminBean = blockedByAdminBean;
    }

    public AddressBean getAddressBean() {
        return addressBean;
    }

    public void setAddressBean(AddressBean addressBean) {
        this.addressBean = addressBean;
    }

    public OccupationBean getOccupationBean() {
        return occupationBean;
    }

    public void setOccupationBean(OccupationBean occupationBean) {
        this.occupationBean = occupationBean;
    }

    public Set<HealthIssueBean> getHealthIssueBeans() {
        return healthIssueBeans;
    }

    public void setHealthIssueBeans(Set<HealthIssueBean> healthIssueBeans) {
        this.healthIssueBeans = healthIssueBeans;
    }
}
