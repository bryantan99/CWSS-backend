package com.chis.communityhealthis.repository.holiday;

import com.chis.communityhealthis.bean.HolidayBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.Date;
import java.util.List;

public interface HolidayDao extends GenericDao<HolidayBean, Integer> {
    HolidayBean findHolidayByDate(Date date);
    List<HolidayBean> findHolidayByYear(String year);
    int deleteHolidayById(Integer holidayId);
}
