package com.chis.communityhealthis.repository.post;

import com.chis.communityhealthis.bean.PostBean;
import com.chis.communityhealthis.repository.GenericDao;

import java.util.List;

public interface PostDao extends GenericDao<PostBean,Integer> {
    List<PostBean> getPosts();
    List<PostBean> getPostsWithMedia();
    PostBean getPostWithMedia(Integer postId);
}
