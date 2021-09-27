package com.chis.communityhealthis.repository.holiday;

import com.chis.communityhealthis.bean.HolidayBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.Date;

public interface HolidayDao extends GenericDao<HolidayBean, Integer> {
    HolidayBean findHolidayByDate(Date date);
}
