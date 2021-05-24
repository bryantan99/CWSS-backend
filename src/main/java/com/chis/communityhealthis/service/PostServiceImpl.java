package com.chis.communityhealthis.service;

import com.chis.communityhealthis.bean.PostBean;
import com.chis.communityhealthis.model.PostForm;
import com.chis.communityhealthis.repository.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService{

    @Autowired
    private PostDao postDao;

    @Override
    public List<PostBean> getPosts() {
        return postDao.getPosts();
    }

    @Override
    public PostBean addPost(PostForm postForm) {
        PostBean bean = new PostBean();
        bean.setPostDescription(postForm.getPostDescription());
        bean.setCreatedBy(postForm.getCreatedBy());
        bean.setCreatedDate(new Date());
        postDao.add(bean);
        return bean;
    }

    @Override
    public void deletePost(Integer postId) {
        PostBean postBean = postDao.find(postId);
        if (postBean == null) {
            return;
        }
        postDao.remove(postBean);
    }
}
