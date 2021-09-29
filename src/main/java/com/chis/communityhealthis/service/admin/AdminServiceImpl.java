package com.chis.communityhealthis.service.admin;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.model.email.MailRequest;
import com.chis.communityhealthis.model.email.template.StaffAccountCreationEmailTemplateModel;
import com.chis.communityhealthis.model.signup.AdminForm;
import com.chis.communityhealthis.model.user.AdminDetailModel;
import com.chis.communityhealthis.repository.account.AccountDao;
import com.chis.communityhealthis.repository.accountrole.AccountRoleDao;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.repository.audit.AuditLogDao;
import com.chis.communityhealthis.repository.auditaction.AuditActionDao;
import com.chis.communityhealthis.service.auth.AuthService;
import com.chis.communityhealthis.service.email.EmailService;
import com.chis.communityhealthis.utility.AuditConstant;
import com.chis.communityhealthis.utility.BeanComparator;
import com.chis.communityhealthis.utility.FlagConstant;
import com.chis.communityhealthis.utility.RoleConstant;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
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
    private AuditLogDao auditLogDao;

    @Autowired
    private AuditActionDao auditActionDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthService authService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AdminDetailModel getAdmin(String username) {
        String currentLoggedInUsername = authService.getCurrentLoggedInUsername();
        Boolean isSuperAdmin = authService.hasRole(RoleConstant.SUPER_ADMIN);

        AdminBean adminBean = adminDao.find(username);
        Assert.notNull(adminBean, "AdminBean [username: " + username + "] was not found!");

        AccountBean accountBean = accountDao.find(username);
        Assert.notNull(accountBean, "AccountBean [username: " + username + "] was not found!");

        AdminDetailModel adminDetailModel = new AdminDetailModel();
        adminDetailModel.setUsername(username);
        adminDetailModel.setFullName(adminBean.getFullName());
        adminDetailModel.setEmail(accountBean.getEmail());
        adminDetailModel.setContactNo(adminBean.getContactNo());

        Boolean deletable = isSuperAdmin && !StringUtils.equals(currentLoggedInUsername, adminBean.getUsername());
        adminDetailModel.setDeletable(deletable);

        return adminDetailModel;
    }

    @Override
    public List<AdminDetailModel> findAllAdmins() {
        Boolean isSuperAdmin = authService.hasRole(RoleConstant.SUPER_ADMIN);
        String currentLoggedInUsername = authService.getCurrentLoggedInUsername();

        List<AdminDetailModel> list = new ArrayList<>();

        List<AdminBean> adminBeans = adminDao.getAll();
        if (CollectionUtils.isEmpty(adminBeans)) {
            return list;
        }

        List<String> usernames = adminBeans.stream().map(AdminBean::getUsername).collect(Collectors.toList());
        List<AccountBean> accountBeans = accountDao.findAccounts(usernames);
        if (CollectionUtils.isEmpty(accountBeans)) {
            return list;
        }
        Map<String, AccountBean> map = new HashMap<>();
        for (AccountBean accountBean: accountBeans) {
            if (!map.containsKey(accountBean.getUsername())) {
                map.put(accountBean.getUsername(), accountBean);
            }
        }

        for (AdminBean adminBean : adminBeans) {
            AccountBean accountBean = map.get(adminBean.getUsername());

            AdminDetailModel model = new AdminDetailModel();
            model.setUsername(adminBean.getUsername());
            model.setFullName(adminBean.getFullName());
            model.setEmail(accountBean.getEmail());
            model.setContactNo(adminBean.getContactNo());

            Boolean deletable = isSuperAdmin && !StringUtils.equals(currentLoggedInUsername, adminBean.getUsername());
            model.setDeletable(deletable);
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

        StaffAccountCreationEmailTemplateModel model = new StaffAccountCreationEmailTemplateModel(form.getFullName(), form.getUsername(), generatedPw);
        sendAccountCreationEmail(form.getEmail(), model);
        return adminBean;
    }

    private void sendAccountCreationEmail(String recipientEmail, StaffAccountCreationEmailTemplateModel model) {
        MailRequest mailRequest = new MailRequest();
        mailRequest.setTemplateFileName("staff-account-creation.ftl");
        mailRequest.setTo(recipientEmail);
        mailRequest.setFrom("communityservicecentre.aulong@gmail.com");
        mailRequest.setSubject("Creation of Staff Account");

        Map<String, Object> map = new HashMap<>();
        map.put("fullName", model.getFullName());
        map.put("username", model.getUsername());
        map.put("defaultPw", model.getGeneratedPw());

        emailService.sendEmailWithTemplate(mailRequest, map);
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

    @Override
    public void updateAdmin(AdminForm adminForm) {
        AuditBean auditBean = new AuditBean(
                AuditConstant.MODULE_ADMIN,
                StringUtils.replace(AuditConstant.ACTION_UPDATE_ADMIN, "%username%", adminForm.getUsername()),
                adminForm.getCreatedBy()
        );
        List<AuditActionBean> auditActionBeans = new ArrayList<>();

        AdminBean adminBean = adminDao.find(adminForm.getUsername());
        Assert.notNull(adminBean, "AdminBean [username: " + adminForm.getUsername() + "] was not found!");
        AdminBean adminBeanDeepCopy = SerializationUtils.clone(adminBean);

        AccountBean accountBean = accountDao.findAccount(adminForm.getUsername());
        Assert.notNull(accountBean, "AccountBean [username: " + adminForm.getUsername() + "] was not found!");
        AccountBean accountBeanDeepCopy = SerializationUtils.clone(accountBean);

        adminBean.setContactNo(adminForm.getContactNo());
        adminDao.update(adminBean);
        BeanComparator adminBeanComparator = new BeanComparator(adminBeanDeepCopy, adminBean);

        accountBean.setEmail(adminForm.getEmail());
        accountDao.update(accountBean);
        BeanComparator accountBeanComparator = new BeanComparator(accountBeanDeepCopy, accountBean);

        auditActionBeans.add(new AuditActionBean(adminBeanComparator.toPrettyString()));
        auditActionBeans.add(new AuditActionBean(accountBeanComparator.toPrettyString()));
        saveLog(auditBean, auditActionBeans);
    }

    private void saveLog(AuditBean auditBean, List<AuditActionBean> auditActionBeans) {
        Integer auditId = auditLogDao.add(auditBean);
        if (!CollectionUtils.isEmpty(auditActionBeans)) {
            for (AuditActionBean bean : auditActionBeans) {
                if (StringUtils.isNotBlank(bean.getActionDescription())) {
                    bean.setAuditId(auditId);
                    auditActionDao.add(bean);
                }
            }
        }
    }

    private AccountBean createAccountBean(AdminForm form) {
        AccountBean bean = new AccountBean();
        bean.setUsername(form.getUsername());
        bean.setIsActive(FlagConstant.YES);
        bean.setPw(bCryptPasswordEncoder.encode(form.getPassword()));
        bean.setEmail(form.getEmail());
        return bean;
    }

    private AdminBean createAdminBean(AdminForm form) {
        AdminBean bean = new AdminBean();
        bean.setFullName(form.getFullName());
        bean.setUsername(form.getUsername());
        bean.setContactNo(form.getContactNo());
        return bean;
    }
}
