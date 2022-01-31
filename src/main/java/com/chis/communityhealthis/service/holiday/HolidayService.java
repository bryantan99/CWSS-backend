package com.chis.communityhealthis.service.holiday;

import com.chis.communityhealthis.model.holiday.HolidayForm;
import com.chis.communityhealthis.model.holiday.HolidayModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface HolidayService {
    Integer insertRecordsUsingExcel(MultipartFile file) throws Exception;
    List<Date> getHolidayDates();
    List<HolidayModel> getHolidayModelList(String year);
    HolidayModel getHolidayModel(Integer holidayId) throws Exception;
    void deleteHoliday(Integer holidayId) throws Exception;
    void updateHoliday(HolidayForm holidayId) throws Exception;
    void addHoliday(HolidayForm form);
}
