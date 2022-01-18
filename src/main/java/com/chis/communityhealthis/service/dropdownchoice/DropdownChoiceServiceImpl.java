package com.chis.communityhealthis.service.dropdownchoice;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.model.dropdown.DropdownChoiceModel;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.repository.appointment.AppointmentDao;
import com.chis.communityhealthis.repository.assistancecategory.AssistanceCategoryDao;
import com.chis.communityhealthis.repository.audit.AuditLogDao;
import com.chis.communityhealthis.repository.communityuser.CommunityUserDao;
import com.chis.communityhealthis.repository.disease.DiseaseDao;
import com.chis.communityhealthis.repository.holiday.HolidayDao;
import com.chis.communityhealthis.repository.zone.ZoneDao;
import com.chis.communityhealthis.utility.AuditConstant;
import com.chis.communityhealthis.utility.DatetimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

    @Autowired
    private ZoneDao zoneDao;

    @Autowired
    private AssistanceCategoryDao assistanceCategoryDao;

    @Autowired
    private AuditLogDao auditLogDao;


    @Override
    public List<DropdownChoiceModel<String>> getDiseaseDropdownList() {
        List<DiseaseBean> beans = diseaseDao.getAll();
        List<DropdownChoiceModel<String>> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(beans)) {
            for (DiseaseBean bean : beans) {
                list.add(new DropdownChoiceModel<>(bean.getDiseaseId().toString(), StringUtils.capitalize(bean.getDiseaseName())));
            }
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public List<DropdownChoiceModel<String>> getAdminUsernameList() {
        List<DropdownChoiceModel<String>> list = new ArrayList<>();
        List<AdminBean> adminBeans = adminDao.getAll();
        if (!CollectionUtils.isEmpty(adminBeans)) {
            for (AdminBean adminBean : adminBeans) {
                list.add(new DropdownChoiceModel<>(adminBean.getUsername(), adminBean.getFullName().toUpperCase()));
            }
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public List<DropdownChoiceModel<String>> getCommunityUserUsernameList(boolean hasNric) {
        List<DropdownChoiceModel<String>> list = new ArrayList<>();
        List<CommunityUserBean> userBeans = communityUserDao.getAllCommunityUsers();
        if (!CollectionUtils.isEmpty(userBeans)) {
            for (CommunityUserBean userBean : userBeans) {
                DropdownChoiceModel<String> model;
                if (hasNric) {
                    model = new DropdownChoiceModel<>(userBean.getUsername(), WordUtils.capitalizeFully(userBean.getFullName()) + " (" + userBean.getNric()  + ")");
                } else {
                    model = new DropdownChoiceModel<>(userBean.getUsername(), WordUtils.capitalizeFully(userBean.getFullName()));
                }
                list.add(model);
            }
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public List<DropdownChoiceModel<Date>> getAppointmentAvailableTimeslot(Date date, String adminUsername, String username) {
        List<DropdownChoiceModel<Date>> list = new ArrayList<>();
        if (!DatetimeUtil.isWeekend(date) && !isPublicHoliday(date)) {
            if (StringUtils.isNotEmpty(username) || StringUtils.isNotEmpty(adminUsername)) {
                return initAdminAndCommunityUserAvailableTimeslots(date, adminUsername, username);
            } else {
                return initDefaultTimeslots(date);
            }
        }
        return list;
    }

    private List<DropdownChoiceModel<Date>> initAdminAndCommunityUserAvailableTimeslots(Date date, String adminUsername, String username) {
        List<AppointmentBean> list = new ArrayList<>();
        List<AppointmentBean> adminAppointments = appointmentDao.getAdminAppointments(adminUsername, date);
        if (!CollectionUtils.isEmpty(adminAppointments)) {
            list.addAll(adminAppointments);
        }
        List<AppointmentBean> userAppointments = appointmentDao.getCommunityUserAppointments(username, date);
        if (!CollectionUtils.isEmpty(userAppointments)) {
            list.addAll(userAppointments);
        }

        List<DropdownChoiceModel<Date>> outputList = initDefaultTimeslots(date);
        if (!CollectionUtils.isEmpty(list)) {
            for (AppointmentBean appointmentBean : list) {
                outputList.removeIf(obj -> obj.getValue().equals(appointmentBean.getAppointmentStartTime()));
            }
        }
        return outputList;
    }

    @Override
    public List<DropdownChoiceModel<String>> getZoneIdsList() {
        List<DropdownChoiceModel<String>> list = new ArrayList<>();
        List<ZoneBean> zoneBeans = zoneDao.getZoneDropdownChoices();
        if (!CollectionUtils.isEmpty(zoneBeans)) {
            for (ZoneBean zoneBean : zoneBeans) {
                list.add(new DropdownChoiceModel<>(zoneBean.getZoneId().toString(), WordUtils.capitalizeFully(zoneBean.getZoneName())));
            }
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public List<DropdownChoiceModel<String>> getAssistanceCategoryDropdownChoices() {
        List<DropdownChoiceModel<String>> list = new ArrayList<>();
        List<AssistanceCategoryBean> categoryBeans = assistanceCategoryDao.getAll();
        if (!CollectionUtils.isEmpty(categoryBeans)) {
            for (AssistanceCategoryBean bean : categoryBeans) {
                list.add(new DropdownChoiceModel<>(bean.getCategoryId().toString(), WordUtils.capitalizeFully(bean.getCategoryName())));
            }
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public List<DropdownChoiceModel<String>> getModuleDropdownChoices() {
        List<DropdownChoiceModel<String>> list = new ArrayList<>();
        List<String> moduleList = AuditConstant.MODULE_LIST;
        if (!CollectionUtils.isEmpty(moduleList)) {
            for (String module : moduleList) {
                list.add(new DropdownChoiceModel<>(module, WordUtils.capitalizeFully(module)));
            }
        }
        Collections.sort(list);
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
                endCalendar.add(Calendar.MINUTE, 30);

                String text = DatetimeUtil.to12HourString(startCalendar) + " - " + DatetimeUtil.to12HourString(endCalendar);
                list.add(new DropdownChoiceModel<>(value, text));
            }
            startCalendar.add(Calendar.MINUTE, 30);
        }

        return list;
    }

    private boolean isPublicHoliday(Date date) {
        HolidayBean holidayBean = holidayDao.findHolidayByDate(date);
        return holidayBean != null;
    }
}
