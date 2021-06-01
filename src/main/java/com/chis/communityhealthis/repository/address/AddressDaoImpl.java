package com.chis.communityhealthis.repository.address;

import com.chis.communityhealthis.bean.AddressBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class AddressDaoImpl extends GenericDaoImpl<AddressBean, String> implements AddressDao {
}
