package com.chis.communityhealthis.service.admin;

import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.model.signup.AdminForm;
import com.chis.communityhealthis.model.user.CommunityUserTableModel;

import java.util.List;

public interface AdminService {
    List<CommunityUserTableModel> findAllAdmins();
    AdminBean addStaff(AdminForm form);
    void deleteStaff(String username);
}
