package com.chis.communityhealthis.model.post;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostForm {
    private Integer postId;
    private String postDescription;
    private String createdBy;
    private List<MultipartFile> fileList;
    private List<Integer> postMediaIdsToBeDeleted;

    public Integer getPostId() {return postId;}

    public void setPostId(Integer postId) {this.postId = postId;}

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<MultipartFile> getFileList() { return fileList; }

    public void setFileList(List<MultipartFile> fileList) { this.fileList = fileList; }

    public List<Integer> getPostMediaIdsToBeDeleted() {
        return postMediaIdsToBeDeleted;
    }

    public void setPostMediaIdsToBeDeleted(List<Integer> postMediaIdsToBeDeleted) {
        this.postMediaIdsToBeDeleted = postMediaIdsToBeDeleted;
    }
}
