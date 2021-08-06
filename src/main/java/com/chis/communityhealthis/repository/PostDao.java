package com.chis.communityhealthis.repository;

import com.chis.communityhealthis.bean.PostBean;

import java.util.List;

public interface PostDao extends GenericDao<PostBean,Integer> {
    List<PostBean> getPosts();
    List<PostBean> getPostsWithMedia();
    PostBean getPostWithMedia(Integer postId);
}
