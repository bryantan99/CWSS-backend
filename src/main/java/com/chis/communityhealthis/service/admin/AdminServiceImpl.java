package com.chis.communityhealthis.service.admin;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.bean.AccountRoleBean;
import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.model.signup.AdminForm;
import com.chis.communityhealthis.model.user.CommunityUserTableModel;
import com.chis.communityhealthis.repository.AccountDao;
import com.chis.communityhealthis.repository.accountRole.AccountRoleDao;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.utility.FlagConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService{

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountRoleDao accountRoleDao;

    @Override
    public List<CommunityUserTableModel> findAllAdmins() {
        List<CommunityUserTableModel> list = new ArrayList<>();

        List<AdminBean> adminBeans = adminDao.getAll();
        if (CollectionUtils.isEmpty(adminBeans)) {
            return list;
        }

        List<String> usernames = adminBeans.stream()
                .map(AdminBean::getUsername)
                .collect(Collectors.toList());

        List<AccountBean> accountBeans = accountDao.findAccounts(usernames);
        Map<String, String> accountStatusMap = accountBeans.stream()
                .collect(Collectors.toMap(AccountBean::getUsername, AccountBean::getIsActive, (x,y)-> x + ", " + y, LinkedHashMap::new));

        for (AdminBean adminBean : adminBeans) {
            CommunityUserTableModel model = new CommunityUserTableModel();
            model.setUsername(adminBean.getUsername());
            model.setFullName(adminBean.getFullName());
            model.setEmail(adminBean.getEmail());
            model.setIsActive(accountStatusMap.get(adminBean.getUsername()));
            list.add(model);
        }

        Collections.sort(list);
        return list;
    }

    @Override
    public AdminBean addStaff(AdminForm form) {
        AccountBean accountBean = createAccountBean(form);
        accountDao.add(accountBean);

        AdminBean adminBean = createAdminBean(form);
        adminDao.add(adminBean);

        for (String role : form.getRoleList()) {
            AccountRoleBean roleBean = new AccountRoleBean();
            roleBean.setUsername(accountBean.getUsername());
            roleBean.setRoleName(role);
            accountRoleDao.add(roleBean);
        }
        return adminBean;
    }

    @Override
    public void deleteStaff(String username) {
        List<AccountRoleBean> roles = accountRoleDao.findUserRoles(username);
        if (!CollectionUtils.isEmpty(roles)) {
            for (AccountRoleBean role : roles) {
                accountRoleDao.remove(role);
            }
        }

        AdminBean adminBean = adminDao.find(username);
        adminDao.remove(adminBean);

        AccountBean accountBean = accountDao.find(username);
        accountDao.remove(accountBean);
    }

    private AccountBean createAccountBean(AdminForm form) {
        AccountBean bean = new AccountBean();
        bean.setUsername(form.getUsername());
        bean.setIsActive(FlagConstant.YES);
        bean.setPw("DUMMY_PASSWORD");
        return bean;
    }

    private AdminBean createAdminBean(AdminForm form) {
        AdminBean bean = new AdminBean();
        bean.setFullName(form.getFullName());
        bean.setUsername(form.getUsername());
        bean.setEmail(form.getEmail());
        bean.setContactNo(form.getContactNo());
        return bean;
    }
}
