package com.chis.communityhealthis.service.assistanceComment;

import com.chis.communityhealthis.bean.AssistanceCommentBean;
import com.chis.communityhealthis.model.assistanceComment.AssistanceCommentForm;
import com.chis.communityhealthis.model.assistanceComment.AssistanceCommentModel;

import java.util.List;

public interface AssistanceCommentService {
    List<AssistanceCommentModel> findComments(Integer assistanceId);
    AssistanceCommentBean addComment(AssistanceCommentForm form);
}
