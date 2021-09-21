package com.chis.communityhealthis.service.dropdownChoice;

import com.chis.communityhealthis.model.dropdown.DropdownChoiceModel;

import java.util.List;

public interface DropdownChoiceService {
    List<DropdownChoiceModel> getDiseaseDropdownList();
    List<DropdownChoiceModel> getAdminUsernameList();
}
