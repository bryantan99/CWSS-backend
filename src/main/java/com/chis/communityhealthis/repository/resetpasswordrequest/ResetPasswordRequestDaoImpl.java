package com.chis.communityhealthis.repository.resetpasswordrequest;

import com.chis.communityhealthis.bean.ResetPasswordRequestBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class ResetPasswordRequestDaoImpl extends GenericDaoImpl<ResetPasswordRequestBean, String> implements ResetPasswordRequestDao {
}
