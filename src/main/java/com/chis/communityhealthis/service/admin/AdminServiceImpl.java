package com.chis.communityhealthis.service.admin;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.bean.AccountRoleBean;
import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.model.email.MailRequest;
import com.chis.communityhealthis.model.signup.AdminForm;
import com.chis.communityhealthis.model.user.AdminDetailModel;
import com.chis.communityhealthis.model.user.CommunityUserTableModel;
import com.chis.communityhealthis.repository.AccountDao;
import com.chis.communityhealthis.repository.accountRole.AccountRoleDao;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.service.email.EmailService;
import com.chis.communityhealthis.utility.FlagConstant;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<AdminDetailModel> findAllAdmins() {
        List<AdminDetailModel> list = new ArrayList<>();

        List<AdminBean> adminBeans = adminDao.getAll();
        if (CollectionUtils.isEmpty(adminBeans)) {
            return list;
        }

        for (AdminBean adminBean : adminBeans) {
            AdminDetailModel model = new AdminDetailModel();
            model.setUsername(adminBean.getUsername());
            model.setFullName(adminBean.getFullName());
            model.setEmail(adminBean.getEmail());
            model.setContactNo(adminBean.getContactNo());
            list.add(model);
        }

        Collections.sort(list);
        return list;
    }

    @Override
    public AdminBean addStaff(AdminForm form) {
        String generatedPw = RandomStringUtils.randomAlphanumeric(8, 12);
        form.setPassword(generatedPw);

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

        sendAccountCreationEmail(adminBean, generatedPw);
        return adminBean;
    }

    private void sendAccountCreationEmail(AdminBean adminBean, String generatedPw) {
        MailRequest mailRequest = new MailRequest();
        mailRequest.setTemplateFileName("staff-account-creation.ftl");
        mailRequest.setTo(adminBean.getEmail());
        mailRequest.setFrom("communityservicecentre.aulong@gmail.com");
        mailRequest.setSubject("Creation of Staff Account");

        Map<String, Object> model = new HashMap<>();
        model.put("fullName", adminBean.getFullName());
        model.put("username", adminBean.getUsername());
        model.put("defaultPw", generatedPw);

        emailService.sendEmailWithTemplate(mailRequest, model);
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
        bean.setPw(bCryptPasswordEncoder.encode(form.getPassword()));
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
