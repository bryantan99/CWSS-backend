package com.chis.communityhealthis.service.holiday;

import com.chis.communityhealthis.bean.HolidayBean;
import com.chis.communityhealthis.model.dropdown.DropdownChoiceModel;
import com.chis.communityhealthis.model.holiday.HolidayForm;
import com.chis.communityhealthis.model.holiday.HolidayModel;
import com.chis.communityhealthis.repository.holiday.HolidayDao;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

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
                        case 2:
                            int year = (int) currentCell.getNumericCellValue();
                            holidayBean.setHolidayYear(String.valueOf(year));
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

    @Override
    public List<Date> getHolidayDates() {
        List<HolidayBean> holidayBeans = holidayDao.getAll();
        List<Date> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(holidayBeans)) {
            for (HolidayBean holidayBean: holidayBeans) {
                list.add(holidayBean.getHolidayDate());
            }
        }
        return list;
    }

    @Override
    public List<HolidayModel> getHolidayModelList(String year) {
        List<HolidayBean> holidayBeans = holidayDao.findHolidayByYear(year);
        List<HolidayModel> holidayModels = new ArrayList<>();
        if (!CollectionUtils.isEmpty(holidayBeans)) {
            for (HolidayBean bean: holidayBeans) {
                holidayModels.add(toHolidayModel(bean));
            }
        }
        Collections.sort(holidayModels);
        return holidayModels;
    }

    @Override
    public HolidayModel getHolidayModel(Integer holidayId) throws Exception {
        HolidayBean holidayBean = holidayDao.find(holidayId);
        if (holidayBean == null) {
            throw new Exception("HolidayBean [holidayId: " + holidayId.toString() + "] was not found.");
        }
        return toHolidayModel(holidayBean);
    }

    @Override
    public void deleteHoliday(Integer holidayId) throws Exception {
        holidayDao.deleteHolidayById(holidayId);
    }

    @Override
    public void updateHoliday(HolidayForm form) throws Exception {
        HolidayBean holidayBean = holidayDao.find(form.getHolidayId());
        if (holidayBean == null) {
            throw new Exception("HolidayBean [holidayId: " + form.getHolidayId() + "] was not found.");
        }

        holidayBean.setHolidayName(form.getHolidayName());
        holidayBean.setHolidayDate(form.getHolidayDate());
        holidayBean.setHolidayYear(getYear(form.getHolidayDate()));
        holidayDao.update(holidayBean);
    }

    @Override
    public void addHoliday(HolidayForm form) {
        HolidayBean holidayBean = createHolidayBean(form);
        holidayDao.add(holidayBean);
    }

    private HolidayBean createHolidayBean(HolidayForm form) {
        HolidayBean bean = new HolidayBean();
        bean.setHolidayName(form.getHolidayName());
        bean.setHolidayDate(form.getHolidayDate());
        bean.setHolidayYear(getYear(form.getHolidayDate()));
        return bean;
    }

    private String getYear(Date holidayDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(holidayDate);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    private HolidayModel toHolidayModel(HolidayBean bean) {
        HolidayModel model = new HolidayModel();
        model.setHolidayId(bean.getHolidayId());
        model.setHolidayName(bean.getHolidayName());
        model.setHolidayDate(bean.getHolidayDate());
        return model;
    }
}
