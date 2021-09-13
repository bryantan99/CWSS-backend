package com.chis.communityhealthis.service.assistanceComment;

import com.chis.communityhealthis.bean.AssistanceCommentBean;
import com.chis.communityhealthis.model.assistanceComment.AssistanceCommentForm;
import com.chis.communityhealthis.model.assistanceComment.AssistanceCommentModel;
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
    public List<AssistanceCommentModel> findComments(Integer assistanceId) {
        List<AssistanceCommentModel> list = new ArrayList<>();
        List<AssistanceCommentBean> commentBeans = assistanceCommentDao.findComments(assistanceId);
        if (!CollectionUtils.isEmpty(commentBeans)) {
            for (AssistanceCommentBean bean : commentBeans) {
                list.add(toAssistanceCommentModel(bean));
            }
        }
        return list;
    }

    private AssistanceCommentModel toAssistanceCommentModel(AssistanceCommentBean bean) {
        AssistanceCommentModel model = new AssistanceCommentModel();
        model.setCommentId(bean.getAssistanceCommentId());
        model.setCommentDesc(bean.getCommentDesc());
        model.setCreatedDate(bean.getCreatedDate());

        if (bean.getAdminBean() != null) {
            model.setCreatedBy(bean.getAdminBean().getUsername());
            model.setCreatedByFullName(bean.getAdminBean().getFullName());
        } else if (bean.getCommunityUserBean() != null) {
            model.setCreatedBy(bean.getCommunityUserBean().getUsername());
            model.setCreatedByFullName(bean.getCommunityUserBean().getFullName());
        }

        return model;
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
