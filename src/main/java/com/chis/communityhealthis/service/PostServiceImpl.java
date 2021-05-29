package com.chis.communityhealthis.service;

import com.chis.communityhealthis.bean.PostBean;
import com.chis.communityhealthis.bean.PostMediaBean;
import com.chis.communityhealthis.model.PostForm;
import com.chis.communityhealthis.repository.PostDao;
import com.chis.communityhealthis.repository.PostMediaDao;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional
public class PostServiceImpl implements PostService{

    private static final String POST_MEDIA_DIRECTORY = "./uploads/Post Media";

    @Autowired
    private PostDao postDao;

    @Autowired
    private PostMediaDao postMediaDao;

    @Override
    public List<PostBean> getPostsWithMedia() {
        return postDao.getPostsWithMedia();
    }

    @Override
    public PostBean addPost(PostForm postForm) throws IOException {
        PostBean postBean = createPostBean(postForm.getPostDescription(), postForm.getCreatedBy());
        Integer postId = postDao.add(postBean);

        if (!CollectionUtils.isEmpty(postForm.getFileList())) {
            String directoryName = createFolder(postId);
            List<String> filesUploaded = uploadPostMedia(directoryName, postForm.getFileList());
            Assert.isTrue(filesUploaded.size() == postForm.getFileList().size(), "There's an error when uploading files to folder.");
            saveMediaDetail(postId, postForm.getFileList());
        }

        return postBean;
    }

    private PostBean createPostBean(String postDescription, String createdBy) {
        PostBean bean = new PostBean();
        bean.setPostDescription(postDescription);
        bean.setCreatedBy(createdBy);
        bean.setCreatedDate(new Date());
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

    private String createFolder(Integer postId) {
        String directoryName = POST_MEDIA_DIRECTORY.concat("/").concat(postId.toString());

        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return directoryName;
    }

    private List<String> uploadPostMedia(String directoryName, List<MultipartFile> fileList) throws IOException {
        List<String> filenames = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileList)) {
            for (MultipartFile file : fileList) {
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                Path fileStorage = get(directoryName, filename).toAbsolutePath().normalize();
                copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
                filenames.add(filename);
            }
        }
        return filenames;
    }

    private void saveMediaDetail(Integer postId, List<MultipartFile> fileList) {
        if (!CollectionUtils.isEmpty(fileList)) {
            String directoryName = POST_MEDIA_DIRECTORY.concat("/").concat(postId.toString());
            for (MultipartFile file : fileList) {
                PostMediaBean bean = createPostMediaBean(file, directoryName);
                bean.setPostId(postId);
                postMediaDao.add(bean);
            }
        }
    }

    private PostMediaBean createPostMediaBean(MultipartFile file, String directoryName) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        directoryName = directoryName.concat("/").concat(filename);

        PostMediaBean bean = new PostMediaBean();
        bean.setMediaType(file.getContentType());
        bean.setMediaDirectory(directoryName);
        return bean;
    }
}
