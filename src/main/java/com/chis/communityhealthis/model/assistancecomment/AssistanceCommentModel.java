package com.chis.communityhealthis.model.assistancecomment;

import java.util.Date;
import java.util.List;

public class AssistanceCommentModel {
    private Integer commentId;
    private String commentDesc;
    private String createdBy;
    private String createdByFullName;
    private Date createdDate;
    private List<AssistanceCommentMediaModel> mediaList;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getCommentDesc() {
        return commentDesc;
    }

    public void setCommentDesc(String commentDesc) {
        this.commentDesc = commentDesc;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByFullName() {
        return createdByFullName;
    }

    public void setCreatedByFullName(String createdByFullName) {
        this.createdByFullName = createdByFullName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<AssistanceCommentMediaModel> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<AssistanceCommentMediaModel> mediaList) {
        this.mediaList = mediaList;
    }
}
