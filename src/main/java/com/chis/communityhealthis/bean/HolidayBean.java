package com.chis.communityhealthis.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "holiday")
public class HolidayBean implements Serializable {
    private static final long serialVersionUID = 694432588280964842L;

    public static final String HOLIDAY_ID = "HOLIDAY_ID";
    public static final String HOLIDAY_NAME = "HOLIDAY_NAME";
    public static final String HOLIDAY_DATE = "HOLIDAY_DATE";
    public static final String YEAR = "YEAR";

    @Id
    @Column(name = HOLIDAY_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer holidayId;

    @Column(name = HOLIDAY_NAME)
    private String holidayName;

    @Column(name = HOLIDAY_DATE)
    private Date holidayDate;

    @Column(name = YEAR)
    private String year;

    public Integer getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(Integer holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
