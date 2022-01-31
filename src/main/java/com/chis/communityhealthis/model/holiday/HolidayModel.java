package com.chis.communityhealthis.model.holiday;

import java.util.Date;

public class HolidayModel implements Comparable<HolidayModel> {

    private Integer holidayId;
    private String holidayName;
    private Date holidayDate;

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

    @Override
    public int compareTo(HolidayModel o) {
        return getHolidayDate().compareTo(o.getHolidayDate());
    }
}
