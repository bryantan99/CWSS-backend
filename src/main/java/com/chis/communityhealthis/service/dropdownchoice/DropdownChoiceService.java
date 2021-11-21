package com.chis.communityhealthis.service.dropdownchoice;

import com.chis.communityhealthis.model.dropdown.DropdownChoiceModel;

import java.util.Date;
import java.util.List;

public interface DropdownChoiceService {
    List<DropdownChoiceModel<String>> getDiseaseDropdownList();
    List<DropdownChoiceModel<String>> getAdminUsernameList();
    List<DropdownChoiceModel<String>> getCommunityUserUsernameList(boolean hasNric);
    List<DropdownChoiceModel<Date>> getAppointmentAvailableTimeslot(Date date, String adminUsername);
    List<DropdownChoiceModel<String>> getZoneIdsList();
    List<DropdownChoiceModel<String>> getAssistanceCategoryDropdownChoices();
}
