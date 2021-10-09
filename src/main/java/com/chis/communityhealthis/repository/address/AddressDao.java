package com.chis.communityhealthis.repository.address;

import com.chis.communityhealthis.bean.AddressBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface AddressDao extends GenericDao<AddressBean, String> {
    List<AddressBean> getUsersAddress(List<String> usernameList);
}
