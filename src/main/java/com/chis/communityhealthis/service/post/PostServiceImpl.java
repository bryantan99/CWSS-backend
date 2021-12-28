package com.chis.communityhealthis.service.post;

import com.chis.communityhealthis.bean.AuditActionBean;
import com.chis.communityhealthis.bean.AuditBean;
import com.chis.communityhealthis.bean.PostBean;
import com.chis.communityhealthis.bean.PostMediaBean;
import com.chis.communityhealthis.model.post.PostForm;
import com.chis.communityhealthis.repository.post.PostDao;
import com.chis.communityhealthis.repository.postmedia.PostMediaDao;
import com.chis.communityhealthis.repository.audit.AuditLogDao;
import com.chis.communityhealthis.repository.auditaction.AuditActionDao;
import com.chis.communityhealthis.utility.AuditConstant;
import com.chis.communityhealthis.utility.BeanComparator;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private static final String POST_MEDIA_DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/post";

    @Autowired
    private PostDao postDao;

    @Autowired
    private PostMediaDao postMediaDao;

    @Autowired
    private AuditLogDao auditLogDao;

    @Autowired
    private AuditActionDao auditActionDao;


    @Override
    public PostBean getPostWithMedia(Integer postId) {
        return postDao.getPostWithMedia(postId);
    }

    @Override
    public PostBean updatePost(PostForm postForm) throws IOException {
        List<AuditActionBean> auditActionBeans = new ArrayList<>();
        AuditBean auditBean = initAuditBean(StringUtils.replace(AuditConstant.ACTION_UPDATE_POST, "%postId%", postForm.getPostId().toString()), postForm.getCreatedBy());

        PostBean postBean = postDao.getPostWithMedia(postForm.getPostId());
        Assert.notNull(postBean, "PostBean with ID: " + postForm.getPostId() + " was not found.");
        PostBean deepCopy = SerializationUtils.clone(postBean);

        postBean.setPostDescription(postForm.getPostDescription());
        postDao.update(postBean);

        BeanComparator beanComparator = new BeanComparator(deepCopy, postBean);
        if (beanComparator.hasChanges()) {
            AuditActionBean postBeanChanges = new AuditActionBean();
            postBeanChanges.setActionDescription(beanComparator.toPrettyString());
            auditActionBeans.add(postBeanChanges);
        }

        if (!CollectionUtils.isEmpty(postForm.getPostMediaIdsToBeDeleted())) {
            for (Integer mediaId : postForm.getPostMediaIdsToBeDeleted()) {
                PostMediaBean mediaBean = postMediaDao.find(mediaId);
                Assert.notNull(mediaBean, "PostMediaBean ID: " + mediaId.toString() + " was not found!");

                File file = new File(POST_MEDIA_DIRECTORY.concat("/").concat(postForm.getPostId().toString()).concat("/").concat(mediaBean.getMediaDirectory()));
                Assert.isTrue(file.exists(), file.getName() + " does not exists in " + file.getAbsolutePath());
                boolean fileIsDeleted = file.delete();
                System.out.println("File is deleted: " + fileIsDeleted);
                if (fileIsDeleted) {
                    AuditActionBean auditActionBean = new AuditActionBean();
                    String replacedMediaId = StringUtils.replace(AuditConstant.ACTION_DELETE_POST_MEDIA, "%postMediaId%", mediaBean.getMediaId().toString());
                    String replacedMediaDirectory = StringUtils.replace(replacedMediaId, "%mediaDirectory%", mediaBean.getMediaDirectory());
                    auditActionBean.setActionDescription(replacedMediaDirectory);
                    auditActionBeans.add(auditActionBean);
                    postMediaDao.remove(mediaBean);
                }
            }

        }

        if (!CollectionUtils.isEmpty(postForm.getFileList())) {
            String directoryName = createFolder(postForm.getPostId());
            List<String> filesUploaded = uploadPostMedia(directoryName, postForm.getFileList());
            Assert.isTrue(filesUploaded.size() == postForm.getFileList().size(), "There's an error when uploading files to folder.");
            Map<Integer, PostMediaBean> mediaBeanMap = saveMediaDetail(postForm.getPostId(), postForm.getFileList());

            if (!CollectionUtils.isEmpty(mediaBeanMap)) {
                for (Map.Entry<Integer, PostMediaBean> entry : mediaBeanMap.entrySet()) {
                    AuditActionBean auditActionBean = new AuditActionBean();
                    String replacedMediaId = StringUtils.replace(AuditConstant.ACTION_CREATE_POST_MEDIA, "%postMediaId%", entry.getKey().toString());
                    String replacedMediaName = StringUtils.replace(replacedMediaId, "%mediaDirectory%", entry.getValue().getMediaDirectory());
                    auditActionBean.setActionDescription(replacedMediaName);
                    auditActionBeans.add(auditActionBean);
                }
            }
        }

        saveLog(auditBean, auditActionBeans);
        return postBean;
    }

    @Override
    public List<PostBean> getPostsWithMedia() {
        return postDao.getPostsWithMedia();
    }

    @Override
    public PostBean addPost(PostForm postForm) throws IOException {
        PostBean postBean = createPostBean(postForm.getPostDescription(), postForm.getCreatedBy());
        Integer postId = postDao.add(postBean);
        AuditBean auditBean = initAuditBean(StringUtils.replace(AuditConstant.ACTION_CREATE_POST, "%postId%", postId.toString()), postForm.getCreatedBy());
        List<AuditActionBean> auditActionBeans = new ArrayList<>();

        if (!CollectionUtils.isEmpty(postForm.getFileList())) {
            String directoryName = createFolder(postId);
            List<String> filesUploaded = uploadPostMedia(directoryName, postForm.getFileList());
            Assert.isTrue(filesUploaded.size() == postForm.getFileList().size(), "There's an error when uploading files to folder.");
            Map<Integer, PostMediaBean> mediaBeanMap = saveMediaDetail(postForm.getPostId(), postForm.getFileList());

            if (!CollectionUtils.isEmpty(mediaBeanMap)) {
                for (Map.Entry<Integer, PostMediaBean> entry : mediaBeanMap.entrySet()) {
                    AuditActionBean auditActionBean = new AuditActionBean();
                    String replacedMediaId = StringUtils.replace(AuditConstant.ACTION_CREATE_POST_MEDIA, "%postMediaId%", entry.getKey().toString());
                    String replacedMediaName = StringUtils.replace(replacedMediaId, "%mediaDirectory%", entry.getValue().getMediaDirectory());
                    auditActionBean.setActionDescription(replacedMediaName);
                    auditActionBeans.add(auditActionBean);
                }
            }
        }

        saveLog(auditBean, auditActionBeans);
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
    public void deletePost(Integer postId, String actionMaker) {
        AuditBean auditBean = initAuditBean(StringUtils.replace(AuditConstant.ACTION_DELETE_POST, "%postId%", postId.toString()), actionMaker);
        List<AuditActionBean> auditActionBeans = new ArrayList<>();

        List<PostMediaBean> postMediaBeans = postMediaDao.findMedias(postId);
        if (!CollectionUtils.isEmpty(postMediaBeans)) {
            File directory = new File(POST_MEDIA_DIRECTORY.concat("/").concat(postId.toString()));
            deleteDirectory(directory);
            for (PostMediaBean mediaBean : postMediaBeans) {
                AuditActionBean auditActionBean = new AuditActionBean();
                String replacedMediaId = StringUtils.replace(AuditConstant.ACTION_DELETE_POST_MEDIA, "%postMediaId%", mediaBean.getMediaId().toString());
                String replacedMediaDirectory = StringUtils.replace(replacedMediaId, "%mediaDirectory%", mediaBean.getMediaDirectory());
                auditActionBean.setActionDescription(replacedMediaDirectory);
                auditActionBeans.add(auditActionBean);
                postMediaDao.remove(mediaBean);
            }
        }

        PostBean postBean = postDao.find(postId);
        Assert.notNull(postBean, "PostBean with ID: " + postId + " was not found!");
        postDao.remove(postBean);
        saveLog(auditBean, auditActionBeans);
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    private String createFolder(Integer postId) {
        String directoryName = generateDirectoryName(postId);

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

    private Map<Integer, PostMediaBean> saveMediaDetail(Integer postId, List<MultipartFile> fileList) {
        Map<Integer, PostMediaBean> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(fileList)) {
            for (MultipartFile file : fileList) {
                PostMediaBean bean = createPostMediaBean(file);
                bean.setPostId(postId);
                Integer mediaId = postMediaDao.add(bean);
                if (!map.containsKey(mediaId)) {
                    map.put(mediaId, bean);
                }
            }
        }
        return map;
    }

    private PostMediaBean createPostMediaBean(MultipartFile file) {
        PostMediaBean bean = new PostMediaBean();
        bean.setMediaType(file.getContentType());
        bean.setMediaDirectory(StringUtils.cleanPath(file.getOriginalFilename()));
        return bean;
    }

    private String generateDirectoryName(Integer postId) {
        return POST_MEDIA_DIRECTORY.concat("/").concat(postId.toString());
    }

    private AuditBean initAuditBean(String actionName, String actionBy) {
        AuditBean auditBean = new AuditBean();
        auditBean.setModule(AuditConstant.MODULE_POST);
        auditBean.setActionName(actionName);
        auditBean.setActionBy(actionBy);
        auditBean.setActionDate(new Date());
        return auditBean;
    }

    private void saveLog(AuditBean auditBean, List<AuditActionBean> auditActionBeans) {
        Integer auditId = auditLogDao.add(auditBean);
        if (!CollectionUtils.isEmpty(auditActionBeans)) {
            for (AuditActionBean auditActionBean : auditActionBeans) {
                auditActionBean.setAuditId(auditId);
                auditActionDao.add(auditActionBean);
            }
        }
    }
}
