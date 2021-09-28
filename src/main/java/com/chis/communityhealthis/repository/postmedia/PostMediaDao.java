package com.chis.communityhealthis.repository.postmedia;

import com.chis.communityhealthis.bean.PostMediaBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface PostMediaDao extends GenericDao<PostMediaBean, Integer> {
    List<PostMediaBean> findMedias(Integer postId);
}
