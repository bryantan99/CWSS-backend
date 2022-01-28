package com.chis.communityhealthis.service.assistancecomment;

import com.chis.communityhealthis.bean.AssistanceCommentBean;
import com.chis.communityhealthis.bean.AssistanceCommentMediaBean;
import com.chis.communityhealthis.model.assistancecomment.AssistanceCommentForm;
import com.chis.communityhealthis.model.assistancecomment.AssistanceCommentMediaModel;
import com.chis.communityhealthis.model.assistancecomment.AssistanceCommentModel;
import com.chis.communityhealthis.repository.assistancecomment.AssistanceCommentDao;
import com.chis.communityhealthis.repository.assistancecommentmedia.AssistanceCommentMediaDao;
import com.chis.communityhealthis.service.storage.StorageService;
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

    @Autowired
    private AssistanceCommentMediaDao mediaDao;

    @Autowired
    private StorageService storageService;

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

        if (!CollectionUtils.isEmpty(bean.getMediaBeanSet())) {
            List<AssistanceCommentMediaModel> list = new ArrayList<>();
            for (AssistanceCommentMediaBean mediaBean : bean.getMediaBeanSet()) {
                list.add(toAssistanceCommentMediaModel(mediaBean));
            }
            model.setMediaList(list);
        }

        return model;
    }

    private AssistanceCommentMediaModel toAssistanceCommentMediaModel(AssistanceCommentMediaBean mediaBean) {
        AssistanceCommentMediaModel mediaModel = new AssistanceCommentMediaModel();
        mediaModel.setMediaId(mediaBean.getMediaId());
        mediaModel.setMediaType(mediaBean.getMediaType());
        mediaModel.setCommentId(mediaBean.getAssistanceCommentId());
        mediaModel.setMediaName(mediaBean.getMediaName());
        return mediaModel;
    }

    @Override
    public AssistanceCommentBean addComment(AssistanceCommentForm form) {
        AssistanceCommentBean bean = createAssistanceCommentBean(form);
        Integer commentId = assistanceCommentDao.add(bean);
        if (!CollectionUtils.isEmpty(form.getFileList())) {
            List<AssistanceCommentMediaBean> mediaBeans = storageService.uploadAssistanceCommentMedias(commentId, form.getFileList());
            if (!CollectionUtils.isEmpty(mediaBeans) && mediaBeans.size() == form.getFileList().size()) {
                for (AssistanceCommentMediaBean mediaBean : mediaBeans) {
                    mediaDao.add(mediaBean);
                }
            }
        }
        return bean;
    }

    private AssistanceCommentBean createAssistanceCommentBean(AssistanceCommentForm form) {
        AssistanceCommentBean bean = new AssistanceCommentBean();
        bean.setAssistanceId(form.getAssistanceId());
        bean.setCommentDesc(form.getCommentDesc());
        bean.setCreatedBy(form.getCreatedBy());
        bean.setCreatedDate(form.getCreatedDate());
        return bean;
    }
}
