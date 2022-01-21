package com.chis.communityhealthis.service.communityuser;

import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
import com.chis.communityhealthis.model.user.BlockDetailModel;
import com.chis.communityhealthis.model.user.BlockUserForm;
import com.chis.communityhealthis.model.user.CommunityUserModel;
import com.chis.communityhealthis.model.user.CommunityUserProfileModel;
import com.chis.communityhealthis.model.filter.CommunityUserBeanQuery;
import javassist.NotFoundException;

import java.util.List;

public interface CommunityUserService {
    List<CommunityUserModel> getApprovedCommunityUsers(CommunityUserBeanQuery filter);
    List<CommunityUserModel> getPendingCommunityUsers(CommunityUserBeanQuery filter);
    CommunityUserProfileModel getCommunityUserProfile(String username) throws Exception;
    void approveUserAccount(String username, String actionMakerUsername) throws Exception;
    void rejectUserAccount(String username, String actionMakerUsername) throws Exception;
    void deleteUserAccount(String username, String actionMakerUsername) throws Exception;
    void updateUserAccount(AccountRegistrationForm form, String actionMaker, boolean isAdmin) throws Exception;
    void blockUser(BlockUserForm form) throws NotFoundException;
    void unblockUser(BlockUserForm form) throws NotFoundException;
    BlockDetailModel getBlockDetail(String username) throws NotFoundException;
}
