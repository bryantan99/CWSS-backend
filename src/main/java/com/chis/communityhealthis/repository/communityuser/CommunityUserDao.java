package com.chis.communityhealthis.repository.communityuser;

import com.chis.communityhealthis.bean.CommunityUserBean;
import com.chis.communityhealthis.model.filter.CommunityUserBeanQuery;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface CommunityUserDao extends GenericDao<CommunityUserBean, String> {
    List<CommunityUserBean> getApprovedCommunityUsers(CommunityUserBeanQuery filter);
    CommunityUserBean getCommunityUser(String username);
    CommunityUserBean getCommunityUserByNric(String nric);
    List<CommunityUserBean> getAllCommunityUsers();
    List<CommunityUserBean> getPendingCommunityUsers(CommunityUserBeanQuery filter);
}
