package com.chis.communityhealthis.repository.healthIssue;

import com.chis.communityhealthis.bean.HealthIssueBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class HealthIssueDaoImpl extends GenericDaoImpl<HealthIssueBean, Integer> implements HealthIssueDao {
}
