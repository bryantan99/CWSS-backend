package com.chis.communityhealthis.repository;

import com.chis.communityhealthis.bean.PostMediaBean;

import java.util.List;

public interface PostMediaDao extends GenericDao<PostMediaBean, Integer> {
    List<PostMediaBean> findMedias(Integer postId);
}
