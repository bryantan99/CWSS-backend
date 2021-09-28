package com.chis.communityhealthis.service.assistancecomment;

import com.chis.communityhealthis.bean.AssistanceCommentBean;
import com.chis.communityhealthis.model.assistancecomment.AssistanceCommentForm;
import com.chis.communityhealthis.model.assistancecomment.AssistanceCommentModel;

import java.util.List;

public interface AssistanceCommentService {
    List<AssistanceCommentModel> findComments(Integer assistanceId);
    AssistanceCommentBean addComment(AssistanceCommentForm form);
}
