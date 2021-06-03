package com.chis.communityhealthis.service.communityuser;

import com.chis.communityhealthis.bean.AccountBean;
import com.chis.communityhealthis.bean.AddressBean;
import com.chis.communityhealthis.bean.CommunityUserBean;
import com.chis.communityhealthis.bean.OccupationBean;
import com.chis.communityhealthis.model.user.CommunityUserProfileModel;
import com.chis.communityhealthis.model.user.CommunityUserTableModel;
import com.chis.communityhealthis.repository.AccountDao;
import com.chis.communityhealthis.repository.address.AddressDao;
import com.chis.communityhealthis.repository.communityuser.CommunityUserDao;
import com.chis.communityhealthis.repository.occupation.OccupationDao;
import com.chis.communityhealthis.utility.FlagConstant;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommunityUserServiceImpl implements CommunityUserService{

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CommunityUserDao communityUserDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private OccupationDao occupationDao;

    @Override
    public List<CommunityUserTableModel> getCommunityUsers() {
        List<CommunityUserTableModel> list = new ArrayList<>();

        List<CommunityUserBean> communityUserBeans = communityUserDao.getAll();
        if (CollectionUtils.isEmpty(communityUserBeans)) {
            return list;
        }

        List<String> usernames = communityUserBeans.stream()
                .map(CommunityUserBean::getUsername)
                .collect(Collectors.toList());

        List<AccountBean> accountBeans = accountDao.findAccounts(usernames);
        Map<String, String> accountStatusMap = accountBeans.stream()
                .collect(Collectors.toMap(AccountBean::getUsername, AccountBean::getIsActive, (x,y)-> x + ", " + y, LinkedHashMap::new));

        for (CommunityUserBean communityUserBean : communityUserBeans) {
            CommunityUserTableModel model = new CommunityUserTableModel();
            model.setUsername(communityUserBean.getUsername());
            model.setFullName(communityUserBean.getFullName());
            model.setEmail(communityUserBean.getEmail());
            model.setIsActive(accountStatusMap.get(communityUserBean.getUsername()));
            list.add(model);
        }

        Collections.sort(list);
        return list;
    }

    @Override
    public CommunityUserProfileModel getCommunityUserProfile(String username) {
        AccountBean accountBean = accountDao.findAccount(username);
        Assert.notNull(accountBean, "Account [username: " + username + "] was not found.");
        CommunityUserBean communityUserBean = communityUserDao.find(username);
        AddressBean addressBean = addressDao.find(username);
        OccupationBean occupationBean = occupationDao.find(username);

        CommunityUserProfileModel profileModel = toCommunityUserProfileModel(communityUserBean, addressBean, occupationBean);
        profileModel.setAccIsActivate(accountBean.getIsActive());
        return profileModel;
    }

    @Override
    public Boolean approveUserAccount(String username) {
        AccountBean accountBean = accountDao.find(username);
        Assert.notNull(accountBean, "Account [username: " + username + "] was not found.");
        accountBean.setIsActive(FlagConstant.YES);
        return true;
    }

    private CommunityUserProfileModel toCommunityUserProfileModel(CommunityUserBean communityUserBean, AddressBean addressBean, OccupationBean occupationBean) {
        CommunityUserProfileModel model = new CommunityUserProfileModel();
        model.setPersonalDetail(communityUserBean);
        model.setAddress(addressBean);
        model.setOccupation(occupationBean);
        return model;
    }
}
