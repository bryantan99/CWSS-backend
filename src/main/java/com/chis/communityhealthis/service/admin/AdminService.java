package com.chis.communityhealthis.service.admin;

import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.model.signup.AdminForm;
import com.chis.communityhealthis.model.user.AdminDetailModel;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    AdminDetailModel getAdmin(String username);
    List<AdminDetailModel> findAllAdmins();
    AdminBean addStaff(AdminForm form) throws IOException;
    void deleteStaff(String username);
    void updateAdmin(AdminForm adminForm) throws Exception;
}
