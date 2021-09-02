package com.chis.communityhealthis.service.admin;

import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.model.signup.AdminForm;
import com.chis.communityhealthis.model.user.AdminDetailModel;

import java.util.List;

public interface AdminService {
    List<AdminDetailModel> findAllAdmins();
    AdminBean addStaff(AdminForm form);
    void deleteStaff(String username);
}
