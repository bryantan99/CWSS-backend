package com.chis.communityhealthis.repository.communityuser;

import com.chis.communityhealthis.bean.CommunityUserBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class CommunityUserDaoImpl extends GenericDaoImpl<CommunityUserBean, String> implements CommunityUserDao {
}
