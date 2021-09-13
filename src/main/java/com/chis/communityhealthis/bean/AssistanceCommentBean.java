package com.chis.communityhealthis.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ASSISTANCE_COMMENT")
public class AssistanceCommentBean implements Serializable {

    private static final long serialVersionUID = 6057354516724090351L;

    public static final String ASSISTANCE_COMMENT_ID = "ASSISTANCE_COMMENT_ID";
    public static final String ASSISTANCE_ID = "ASSISTANCE_ID";
    public static final String COMMENT_DESC = "COMMENT_DESC";
    public static final String CREATED_BY = "CREATED_BY";
    public static final String CREATED_DATE = "CREATED_DATE";

    @Id
    @Column(name = ASSISTANCE_COMMENT_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assistanceCommentId;

    @Column(name = ASSISTANCE_ID)
    private Integer assistanceId;

    @Column(name = COMMENT_DESC)
    private String commentDesc;

    @Column(name = CREATED_BY)
    private String createdBy;

    @Column(name = CREATED_DATE)
    private Date createdDate;

    public Integer getAssistanceCommentId() {
        return assistanceCommentId;
    }

    public void setAssistanceCommentId(Integer assistanceCommentId) {
        this.assistanceCommentId = assistanceCommentId;
    }

    public Integer getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(Integer assistanceId) {
        this.assistanceId = assistanceId;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
