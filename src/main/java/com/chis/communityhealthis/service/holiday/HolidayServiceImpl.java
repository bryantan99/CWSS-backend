package com.chis.communityhealthis.service.holiday;

import com.chis.communityhealthis.bean.HolidayBean;
import com.chis.communityhealthis.repository.holiday.HolidayDao;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Iterator;

@Service
@Transactional
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private HolidayDao holidayDao;

    @Override
    public Integer insertRecordsUsingExcel(MultipartFile file) throws Exception {
        int insertedRecord = 0;

        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet("Sheet1");
            Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                HolidayBean holidayBean = new HolidayBean();
                int cellIndex = 0;

                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIndex) {
                        case 0:
                            holidayBean.setHolidayName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            holidayBean.setHolidayDate(currentCell.getDateCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIndex++;
                }
                Integer holidayId = holidayDao.add(holidayBean);
                if (holidayId != null) {
                    insertedRecord++;
                }
                workbook.close();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return insertedRecord;
    }
}