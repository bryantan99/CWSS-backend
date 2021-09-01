package com.chis.communityhealthis.repository.admin;

import com.chis.communityhealthis.bean.AdminBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDaoImpl extends GenericDaoImpl<AdminBean, String> implements AdminDao {
}
