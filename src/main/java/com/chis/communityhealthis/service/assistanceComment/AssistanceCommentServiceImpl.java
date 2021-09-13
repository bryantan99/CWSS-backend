package com.chis.communityhealthis.service.assistanceComment;

import com.chis.communityhealthis.bean.AssistanceCommentBean;
import com.chis.communityhealthis.model.assistanceComment.AssistanceCommentForm;
import com.chis.communityhealthis.repository.assistanceComment.AssistanceCommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AssistanceCommentServiceImpl implements AssistanceCommentService {

    @Autowired
    private AssistanceCommentDao assistanceCommentDao;

    @Override
    public List<AssistanceCommentBean> findComments(Integer assistanceId) {
        List<AssistanceCommentBean> list = assistanceCommentDao.findComments(assistanceId);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public AssistanceCommentBean addComment(AssistanceCommentForm form) {
        AssistanceCommentBean bean = new AssistanceCommentBean();
        bean.setAssistanceId(form.getAssistanceId());
        bean.setCommentDesc(form.getCommentDesc());
        bean.setCreatedBy(form.getCreatedBy());
        bean.setCreatedDate(form.getCreatedDate());
        assistanceCommentDao.add(bean);
        return bean;
    }
}
