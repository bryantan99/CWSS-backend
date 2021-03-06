package com.chis.communityhealthis.service.admin;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.factory.AuditBeanFactory;
import com.chis.communityhealthis.model.email.MailRequest;
import com.chis.communityhealthis.model.email.template.StaffAccountCreationEmailTemplateModel;
import com.chis.communityhealthis.model.role.RoleModel;
import com.chis.communityhealthis.model.signup.AdminForm;
import com.chis.communityhealthis.model.user.AdminDetailModel;
import com.chis.communityhealthis.repository.account.AccountDao;
import com.chis.communityhealthis.repository.accountrole.AccountRoleDao;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.service.audit.AuditService;
import com.chis.communityhealthis.service.auth.AuthService;
import com.chis.communityhealthis.service.email.EmailService;
import com.chis.communityhealthis.service.storage.StorageService;
import com.chis.communityhealthis.utility.*;
import io.jsonwebtoken.lang.Assert;
import javassist.NotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountRoleDao accountRoleDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${frontend.url}")
    private String FRONTEND_URL;

    @Override
    public AdminDetailModel getAdmin(String username) throws NotFoundException {
        String currentLoggedInUsername = authService.getCurrentLoggedInUsername();
        Boolean isSuperAdmin = authService.hasRole(RoleConstant.SUPER_ADMIN);

        AdminBean adminBean = adminDao.find(username);
        if (adminBean == null) {
            throw new NotFoundException("Admin [username: " + username + "] was not found.");
        }

        AccountBean accountBean = accountDao.findAccountWithRoles(username);
        if (accountBean == null) {
            throw new NotFoundException("Account [username: " + username + "] was not found.");
        }

        AdminDetailModel adminDetailModel = toAdminDetailModel(accountBean, adminBean);
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
        for (AccountBean accountBean : accountBeans) {
            if (!map.containsKey(accountBean.getUsername())) {
                map.put(accountBean.getUsername(), accountBean);
            }
        }

        for (AdminBean adminBean : adminBeans) {
            AccountBean accountBean = map.get(adminBean.getUsername());
            AdminDetailModel model = toAdminDetailModel(accountBean, adminBean);
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

        String fileName = null;
        if (form.getProfilePicFile() != null) {
            fileName = storageService.uploadProfilePicture(form.getUsername(), form.getProfilePicFile());
        }

        AdminBean adminBean = createAdminBean(form, fileName);
        adminDao.add(adminBean);

        for (String role : form.getRoleList()) {
            AccountRoleBean roleBean = new AccountRoleBean();
            roleBean.setUsername(accountBean.getUsername());
            roleBean.setRoleName(role);
            accountRoleDao.add(roleBean);
        }

        AuditBeanFactory auditFactory = new AuditBeanFactory(AuditConstant.MODULE_ADMIN, AuditConstant.formatActionAddAdmin(form.getUsername(), form.getFullName()), form.getCreatedBy());
        AuditBean auditBean = auditFactory.createAuditBean();
        auditService.saveLogs(auditBean, null);

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
        map.put("loginUrl", FRONTEND_URL + "/login");

        emailService.sendEmailWithTemplate(mailRequest, map);
    }

    @Override
    public void deleteStaff(String username, String actionMakerUsername) throws Exception {
        List<AccountRoleBean> roles = accountRoleDao.findUserRoles(username);
        AdminBean adminBean = adminDao.find(username);
        AccountBean accountBean = accountDao.find(username);

        if (accountBean == null || adminBean == null || CollectionUtils.isEmpty(roles)) {
            throw new NotFoundException(username + " record is not found.");
        }

        if (StringUtils.isNotEmpty(adminBean.getProfilePicDirectory())) {
            String relativePathToFolder = DirectoryConstant.AWS_ACCOUNT_PROFILE_PIC_DIRECTORY + "/" + username;
            storageService.deleteFolderWithItsContents(relativePathToFolder);
        }

        for (AccountRoleBean role : roles) {
            accountRoleDao.remove(role);
        }
        adminDao.remove(adminBean);
        accountDao.remove(accountBean);

        AuditBeanFactory auditBeanFactory = new AuditBeanFactory(AuditConstant.MODULE_ADMIN, AuditConstant.formatActionDeleteAdmin(username, adminBean.getFullName()), actionMakerUsername);
        AuditBean auditBean = auditBeanFactory.createAuditBean();
        auditService.saveLogs(auditBean, null);
    }

    @Override
    public void updateAdmin(AdminForm adminForm) {
        AuditBeanFactory auditBeanFactory = new AuditBeanFactory(AuditConstant.MODULE_ADMIN, AuditConstant.formatActionUpdateAdmin(adminForm.getUsername(), adminForm.getFullName()), adminForm.getCreatedBy());
        AuditBean auditBean = auditBeanFactory.createAuditBean();
        List<AuditActionBean> auditActionBeans = new ArrayList<>();

        AdminBean adminBean = adminDao.find(adminForm.getUsername());
        Assert.notNull(adminBean, "AdminBean [username: " + adminForm.getUsername() + "] was not found!");
        AdminBean adminBeanDeepCopy = SerializationUtils.clone(adminBean);

        AccountBean accountBean = accountDao.findAccountWithRoles(adminForm.getUsername());
        Assert.notNull(accountBean, "AccountBean [username: " + adminForm.getUsername() + "] was not found!");
        AccountBean accountBeanDeepCopy = SerializationUtils.clone(accountBean);

        AuditActionBean accountRoleAuditActionBean = updateAccountRoles(adminForm, accountBean);
        if (accountRoleAuditActionBean != null) {
            auditActionBeans.add(accountRoleAuditActionBean);
        }

        adminBean.setFullName(adminForm.getFullName());
        adminBean.setContactNo(adminForm.getContactNo());
        if (adminForm.getProfilePicFile() != null) {
            if (StringUtils.isNotEmpty(adminBean.getProfilePicDirectory())) {
                String fileNameToBeDeleted = DirectoryConstant.AWS_ACCOUNT_PROFILE_PIC_DIRECTORY + "/" + adminBean.getUsername() + "/" + adminBean.getProfilePicDirectory();
                storageService.deleteFile(fileNameToBeDeleted);
            }
            String newFileName = storageService.uploadProfilePicture(adminForm.getUsername(), adminForm.getProfilePicFile());
            adminBean.setProfilePicDirectory(newFileName);
        }
        adminDao.update(adminBean);
        BeanComparator adminBeanComparator = new BeanComparator(adminBeanDeepCopy, adminBean);

        accountBean.setEmail(adminForm.getEmail());
        accountBean.setIsActive(adminForm.getIsActive());
        accountDao.update(accountBean);
        BeanComparator accountBeanComparator = new BeanComparator(accountBeanDeepCopy, accountBean);

        if (adminBeanComparator.hasChanges()) {
            auditActionBeans.add(new AuditActionBean(adminBeanComparator.toPrettyString()));
        }
        if (accountBeanComparator.hasChanges()) {
            auditActionBeans.add(new AuditActionBean(accountBeanComparator.toPrettyString()));
        }

        auditService.saveLogs(auditBean, auditActionBeans);
    }

    private AuditActionBean updateAccountRoles(AdminForm adminForm, AccountBean accountBean) {
        List<String> roleIds = accountBean.getRoles().stream().map(AccountRoleBean::getRoleName).collect(Collectors.toList());
        List<String> newRoleIds = adminForm.getRoleList();
        BeanComparator accountRolesComparator = new BeanComparator(roleIds, newRoleIds);
        if (!accountRolesComparator.hasChanges()) {
            return null;
        }

        List<String> rolesToDelete = differenceBetweenList(roleIds, newRoleIds);
        List<String> rolesToAdd = differenceBetweenList(newRoleIds, roleIds);
        Set<AccountRoleBean> currentAccountRoles = accountBean.getRoles();

        if (!CollectionUtils.isEmpty(currentAccountRoles)) {
            for (AccountRoleBean bean : currentAccountRoles) {
                if (rolesToDelete.contains(bean.getRoleName())) {
                    accountRoleDao.remove(bean);
                }
            }
        }

        if (!CollectionUtils.isEmpty(rolesToAdd)) {
            for (String role : rolesToAdd) {
                AccountRoleBean roleBean = new AccountRoleBean();
                roleBean.setRoleName(role);
                roleBean.setUsername(adminForm.getUsername());
                accountRoleDao.add(roleBean);
            }
        }

        AuditActionBean auditActionBean = new AuditActionBean();
        auditActionBean.setActionDescription("Diff:\n* changes on account roles\n - current account roles : " + adminForm.getRoleList());
        return auditActionBean;
    }

    private List<String> differenceBetweenList(List<String> list1, List<String> list2) {
        List<String> differences = new ArrayList<>(list1);
        differences.removeAll(list2);
        return differences;
    }

    private AccountBean createAccountBean(AdminForm form) {
        AccountBean bean = new AccountBean();
        bean.setUsername(form.getUsername());
        bean.setIsActive(FlagConstant.YES);
        bean.setPw(bCryptPasswordEncoder.encode(form.getPassword()));
        bean.setEmail(form.getEmail());
        return bean;
    }

    private AdminBean createAdminBean(AdminForm form, String profilePicDirectory) {
        AdminBean bean = new AdminBean();
        bean.setFullName(form.getFullName());
        bean.setUsername(form.getUsername());
        bean.setContactNo(form.getContactNo());
        if (StringUtils.isNotBlank(profilePicDirectory)) {
            bean.setProfilePicDirectory(profilePicDirectory);
        }
        return bean;
    }

    private AdminDetailModel toAdminDetailModel(AccountBean accountBean, AdminBean adminBean) {
        AdminDetailModel adminDetailModel = new AdminDetailModel();
        adminDetailModel.setUsername(accountBean.getUsername());
        adminDetailModel.setFullName(adminBean.getFullName());
        adminDetailModel.setEmail(accountBean.getEmail());
        adminDetailModel.setContactNo(adminBean.getContactNo());
        adminDetailModel.setIsActive(accountBean.getIsActive());
        adminDetailModel.setProfilePicDirectory(adminBean.getProfilePicDirectory());
        if (!CollectionUtils.isEmpty(accountBean.getRoles())) {
            List<RoleModel> roleList = new ArrayList<>();
            RoleConstant roleConstant = new RoleConstant();
            for (AccountRoleBean roleBean : accountBean.getRoles()) {
                String formattedName = roleConstant.getRoleFullName(roleBean.getRoleName());
                RoleModel roleModel = new RoleModel();
                roleModel.setRoleId(roleBean.getRoleName());
                roleModel.setRoleName(formattedName);
                roleList.add(roleModel);
            }
            adminDetailModel.setRoleList(roleList);
        }
        return adminDetailModel;
    }
}
