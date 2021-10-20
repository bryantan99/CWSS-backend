package com.chis.communityhealthis.service.dropdownchoice;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.model.dropdown.DropdownChoiceModel;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.repository.appointment.AppointmentDao;
import com.chis.communityhealthis.repository.communityuser.CommunityUserDao;
import com.chis.communityhealthis.repository.disease.DiseaseDao;
import com.chis.communityhealthis.repository.holiday.HolidayDao;
import com.chis.communityhealthis.utility.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DropdownChoiceServiceImpl implements DropdownChoiceService {

    @Autowired
    private DiseaseDao diseaseDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private CommunityUserDao communityUserDao;

    @Autowired
    private HolidayDao holidayDao;

    @Autowired
    private AppointmentDao appointmentDao;

    @Override
    public List<DropdownChoiceModel<String>> getDiseaseDropdownList() {
        List<DiseaseBean> beans = diseaseDao.getAll();
        List<DropdownChoiceModel<String>> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(beans)) {
            for (DiseaseBean bean : beans) {
                list.add(new DropdownChoiceModel<String>(bean.getDiseaseId().toString(), StringUtils.capitalize(bean.getDiseaseName())));
            }
        }
        return list;
    }

    @Override
    public List<DropdownChoiceModel<String>> getAdminUsernameList() {
        List<DropdownChoiceModel<String>> list = new ArrayList<>();
        List<AdminBean> adminBeans = adminDao.getAll();
        if (!CollectionUtils.isEmpty(adminBeans)) {
            for (AdminBean adminBean : adminBeans) {
                list.add(new DropdownChoiceModel<String>(adminBean.getUsername(), adminBean.getFullName().toUpperCase()));
            }
        }
        return list;
    }

    @Override
    public List<DropdownChoiceModel<String>> getCommunityUserUsernameList() {
        List<DropdownChoiceModel<String>> list = new ArrayList<>();
        List<CommunityUserBean> userBeans = communityUserDao.getAll();
        if (!CollectionUtils.isEmpty(userBeans)) {
            for (CommunityUserBean userBean : userBeans) {
                list.add(new DropdownChoiceModel<String>(userBean.getUsername(), userBean.getFullName().toUpperCase()));
            }
        }
        return list;
    }

    @Override
    public List<DropdownChoiceModel<Date>> getAppointmentAvailableTimeslot(Date date, String adminUsername) {
        List<DropdownChoiceModel<Date>> list = new ArrayList<>();

        if (!DatetimeUtil.isWeekend(date) && !isPublicHoliday(date)) {
            if (StringUtils.isNotBlank(adminUsername)) {
                return initAdminAvailableTimeslots(date, adminUsername);
            } else {
                return initDefaultTimeslots(date);
            }
        }

        return list;
    }

    private List<DropdownChoiceModel<Date>> initAdminAvailableTimeslots(Date date, String adminUsername) {
        List<AppointmentBean> appointmentBeanList = appointmentDao.getAdminAppointments(adminUsername, date);
        List<DropdownChoiceModel<Date>> list = initDefaultTimeslots(date);

        if (!CollectionUtils.isEmpty(appointmentBeanList)) {
            for (AppointmentBean appointmentBean : appointmentBeanList) {
                list.removeIf(obj -> obj.getValue().equals(appointmentBean.getAppointmentStartTime()));
            }
        }

        return list;
    }

    private List<DropdownChoiceModel<Date>> initDefaultTimeslots(Date date) {
        List<DropdownChoiceModel<Date>> list = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        startCalendar.set(Calendar.HOUR_OF_DAY, 9);
        startCalendar.set(Calendar.MINUTE, 30);

        while (startCalendar.get(Calendar.HOUR_OF_DAY) < 16) {
            if (!DatetimeUtil.isLunchHour(startCalendar)) {
                Date value = startCalendar.getTime();
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(startCalendar.getTime());
                endCalendar.add(Calendar.HOUR_OF_DAY, 1);

                String text = DatetimeUtil.to12HourString(startCalendar) + " - " + DatetimeUtil.to12HourString(endCalendar);
                list.add(new DropdownChoiceModel<Date>(value, text));
            }
            startCalendar.add(Calendar.HOUR_OF_DAY, 1);
        }

        return list;
    }

    private boolean isPublicHoliday(Date date) {
        HolidayBean holidayBean = holidayDao.findHolidayByDate(date);
        return holidayBean !=  null;
    }
}
