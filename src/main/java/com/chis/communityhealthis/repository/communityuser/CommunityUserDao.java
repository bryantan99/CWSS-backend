package com.chis.communityhealthis.repository.communityuser;

import com.chis.communityhealthis.bean.CommunityUserBean;
import com.chis.communityhealthis.model.filter.CommunityUserBeanQuery;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface CommunityUserDao extends GenericDao<CommunityUserBean, String> {
    List<CommunityUserBean> getCommunityUsers(CommunityUserBeanQuery filter);
    CommunityUserBean getCommunityUser(String username);
    List<CommunityUserBean> getAllCommunityUsers();
}
