package com.chis.communityhealthis.service.communityuser;

import com.chis.communityhealthis.model.signup.AccountRegistrationForm;
import com.chis.communityhealthis.model.user.CommunityUserProfileModel;
import com.chis.communityhealthis.model.user.CommunityUserTableModel;

import java.util.List;

public interface CommunityUserService {
    List<CommunityUserTableModel> getCommunityUsers();
    CommunityUserProfileModel getCommunityUserProfile(String username);
    Boolean approveUserAccount(String username, String adminUsername);
    Boolean rejectUserAccount(String username);
    void deleteUserAccount(String username);
    void updateUserAccount(AccountRegistrationForm form);
}
