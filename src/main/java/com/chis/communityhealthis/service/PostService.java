package com.chis.communityhealthis.service;

import com.chis.communityhealthis.bean.PostBean;
import com.chis.communityhealthis.model.PostForm;

import java.util.List;

public interface PostService {
    List<PostBean> getPosts();
    PostBean addPost(PostForm postForm);
    void deletePost(Integer postId);
}
