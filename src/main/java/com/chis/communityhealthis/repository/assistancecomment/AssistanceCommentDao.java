package com.chis.communityhealthis.repository.assistancecomment;

import com.chis.communityhealthis.bean.AssistanceCommentBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface AssistanceCommentDao extends GenericDao<AssistanceCommentBean, Integer> {
    List<AssistanceCommentBean> findComments(Integer assistanceId);
}
