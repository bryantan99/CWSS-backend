package com.chis.communityhealthis.service.communityuser;

import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
import com.chis.communityhealthis.model.user.CommunityUserModel;
import com.chis.communityhealthis.model.user.CommunityUserProfileModel;
import com.chis.communityhealthis.model.filter.CommunityUserBeanQuery;
import javassist.NotFoundException;

import java.util.List;

public interface CommunityUserService {
    List<CommunityUserModel> getCommunityUsers(CommunityUserBeanQuery filter);
    CommunityUserProfileModel getCommunityUserProfile(String username) throws NotFoundException;
    void approveUserAccount(String username, String adminUsername) throws Exception;
    void rejectUserAccount(String username) throws Exception;
    void deleteUserAccount(String username);
    void updateUserAccount(AccountRegistrationForm form, String actionMaker, boolean isAdmin) throws Exception;
}
