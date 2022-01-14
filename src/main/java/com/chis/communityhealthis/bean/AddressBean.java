package com.chis.communityhealthis.bean;

import org.javers.core.metamodel.annotation.TypeName;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ADDRESS")
@TypeName("AddressBean")
public class AddressBean implements Serializable {

    private static final long serialVersionUID = -5507349330083306948L;

    public static final String USERNAME = "USERNAME";
    public static final String ADDRESS_LINE_1 = "ADDRESS_LINE_1";
    public static final String ADDRESS_LINE_2 = "ADDRESS_LINE_2";
    public static final String POSTCODE = "POSTCODE";
    public static final String CITY = "CITY";
    public static final String STATE = "STATE";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String ZONE_ID = "ZONE_ID";

    @Id
    @Column(name = USERNAME)
    private String username;

    @Column(name = ADDRESS_LINE_1)
    private String addressLine1;

    @Column(name = ADDRESS_LINE_2)
    private String addressLine2;

    @Column(name = POSTCODE)
    private String postcode;

    @Column(name = CITY)
    private String city;

    @Column(name = STATE)
    private String state;

    @Column(name = LATITUDE)
    private Double latitude;

    @Column(name = LONGITUDE)
    private Double longitude;

    @Column(name = ZONE_ID)
    private Integer zoneId;

    @OneToOne
    @JoinColumn(name = ZONE_ID, referencedColumnName = ZoneBean.ZONE_ID, insertable = false, updatable = false)
    private ZoneBean zoneBean;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public ZoneBean getZoneBean() {
        return zoneBean;
    }

    public void setZoneBean(ZoneBean zoneBean) {
        this.zoneBean = zoneBean;
    }
}
