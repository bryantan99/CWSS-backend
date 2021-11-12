package com.chis.communityhealthis.service.holiday;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface HolidayService {
    Integer insertRecordsUsingExcel(MultipartFile file) throws Exception;
    List<Date> getHolidayDates();
}
