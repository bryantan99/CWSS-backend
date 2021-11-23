package com.chis.communityhealthis.service.admin;

import com.chis.communityhealthis.bean.*;
import com.chis.communityhealthis.model.email.MailRequest;
import com.chis.communityhealthis.model.email.template.StaffAccountCreationEmailTemplateModel;
import com.chis.communityhealthis.model.role.RoleModel;
import com.chis.communityhealthis.model.signup.AdminForm;
import com.chis.communityhealthis.model.user.AdminDetailModel;
import com.chis.communityhealthis.repository.account.AccountDao;
import com.chis.communityhealthis.repository.accountrole.AccountRoleDao;
import com.chis.communityhealthis.repository.admin.AdminDao;
import com.chis.communityhealthis.repository.audit.AuditLogDao;
import com.chis.communityhealthis.repository.auditaction.AuditActionDao;
import com.chis.communityhealthis.service.auth.AuthService;
import com.chis.communityhealthis.service.email.EmailService;
import com.chis.communityhealthis.utility.*;
import io.jsonwebtoken.lang.Assert;
import javassist.NotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private static final String ACCOUNT_PROFILE_PIC_DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/account/profile-pic";

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
    public AdminBean addStaff(AdminForm form) throws IOException {
        String generatedPw = RandomStringUtils.randomAlphanumeric(8, 12);
        form.setPassword(generatedPw);

        AccountBean accountBean = createAccountBean(form);
        accountDao.add(accountBean);

        String profilePicDirectory = null;
        if (form.getProfilePicFile() != null) {
            String directoryName = createFolder(form.getUsername());
            List<String> filesUploaded = uploadProfilePicture(directoryName, form.getProfilePicFile());
            Assert.isTrue(filesUploaded.size() == 1, "There's an error when uploading profile picture to folder.");
            profilePicDirectory = org.springframework.util.StringUtils.cleanPath(form.getProfilePicFile().getOriginalFilename());
        }

        AdminBean adminBean = createAdminBean(form, profilePicDirectory);
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

    private List<String> uploadProfilePicture(String directoryName, MultipartFile profilePicFile) throws IOException {
        List<String> filenames = new ArrayList<>();
        if (profilePicFile != null) {
            String filename = org.springframework.util.StringUtils.cleanPath(profilePicFile.getOriginalFilename());
            Path fileStorage = get(directoryName, filename).toAbsolutePath().normalize();
            copy(profilePicFile.getInputStream(), fileStorage, REPLACE_EXISTING);
            filenames.add(filename);
        }
        return filenames;
    }

    private String createFolder(String username) {
        String directoryName = ACCOUNT_PROFILE_PIC_DIRECTORY.concat("/").concat(username);
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directoryName;
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
    public void updateAdmin(AdminForm adminForm) throws Exception {
        AuditBean auditBean = new AuditBean(
                AuditConstant.MODULE_ADMIN,
                StringUtils.replace(AuditConstant.ACTION_UPDATE_ADMIN, "%username%", adminForm.getUsername()),
                adminForm.getCreatedBy()
        );
        List<AuditActionBean> auditActionBeans = new ArrayList<>();

        AdminBean adminBean = adminDao.find(adminForm.getUsername());
        Assert.notNull(adminBean, "AdminBean [username: " + adminForm.getUsername() + "] was not found!");
        AdminBean adminBeanDeepCopy = SerializationUtils.clone(adminBean);

        AccountBean accountBean = accountDao.findAccountWithRoles(adminForm.getUsername());
        Assert.notNull(accountBean, "AccountBean [username: " + adminForm.getUsername() + "] was not found!");
        AccountBean accountBeanDeepCopy = SerializationUtils.clone(accountBean);

        List<String> roleIds = accountBean.getRoles().stream().map(AccountRoleBean::getRoleName).collect(Collectors.toList());
        List<String> newRoleIds = adminForm.getRoleList();
        List<String> rolesToDelete = differenceBetweenList(roleIds, newRoleIds);
        List<String> rolesToAdd = differenceBetweenList(newRoleIds, roleIds);
        updateAdminRoles(adminBean.getUsername(), rolesToAdd, rolesToDelete);

        adminBean.setFullName(adminForm.getFullName());
        adminBean.setContactNo(adminForm.getContactNo());
        if (adminForm.getProfilePicFile() != null) {
            String directoryName = DirectoryConstant.ACCOUNT_PROFILE_PIC_DIRECTORY.concat("/").concat(adminForm.getUsername());
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directoryName = createFolder(adminForm.getUsername());
            } else {
                deleteProfilePicture(directoryName);
            }
            try {
                uploadProfilePicture(directoryName, adminForm.getProfilePicFile());
            } catch (IOException e) {
                throw new Exception(e.getMessage());
            }
            String profilePicDirectory = org.springframework.util.StringUtils.cleanPath(adminForm.getProfilePicFile().getOriginalFilename());
            adminBean.setProfilePicDirectory(profilePicDirectory);
        }
        adminDao.update(adminBean);
        BeanComparator adminBeanComparator = new BeanComparator(adminBeanDeepCopy, adminBean);

        accountBean.setEmail(adminForm.getEmail());
        accountDao.update(accountBean);
        BeanComparator accountBeanComparator = new BeanComparator(accountBeanDeepCopy, accountBean);

        auditActionBeans.add(new AuditActionBean(adminBeanComparator.toPrettyString()));
        auditActionBeans.add(new AuditActionBean(accountBeanComparator.toPrettyString()));
        saveLog(auditBean, auditActionBeans);
    }

    private void updateAdminRoles(String username, List<String> rolesToAdd, List<String> rolesToDelete) {
        List<AccountRoleBean> list = accountRoleDao.findUserRoles(username);
        if (!CollectionUtils.isEmpty(list)) {
            for (AccountRoleBean bean : list) {
                if (rolesToDelete.contains(bean.getRoleName())) {
                    accountRoleDao.remove(bean);
                }
            }
        }

        for (String newRole: rolesToAdd) {
            AccountRoleBean roleBean = new AccountRoleBean();
            roleBean.setUsername(username);
            roleBean.setRoleName(newRole);
            accountRoleDao.add(roleBean);
        }
    }

    private List<String> differenceBetweenList(List<String> list1, List<String> list2) {
        List<String> differences = new ArrayList<>(list1);
        differences.removeAll(list2);
        return differences;
    }

    private void deleteProfilePicture(String directoryName) {
        File directory = new File(directoryName);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    boolean isDeleted = file.delete();
                }
            }
        }
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
