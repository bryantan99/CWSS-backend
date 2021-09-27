package com.chis.communityhealthis.service.holiday;

import org.springframework.web.multipart.MultipartFile;

public interface HolidayService {
    Integer insertRecordsUsingExcel(MultipartFile file) throws Exception;
}
