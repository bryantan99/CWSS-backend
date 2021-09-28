package com.chis.communityhealthis.service.post;

import com.chis.communityhealthis.bean.PostBean;
import com.chis.communityhealthis.model.post.PostForm;

import java.io.IOException;
import java.util.List;

public interface PostService {
    List<PostBean> getPostsWithMedia();
    PostBean addPost(PostForm postForm) throws IOException;
    void deletePost(Integer postId, String actionMaker);
    PostBean getPostWithMedia(Integer postId);
    PostBean updatePost(PostForm postFormObj) throws IOException;
}
