package com.chis.communityhealthis.repository.healthIssue;

import com.chis.communityhealthis.bean.HealthIssueBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface HealthIssueDao extends GenericDao<HealthIssueBean, Integer> {
    List<HealthIssueBean> findHealthIssueBeans(String username);
}
