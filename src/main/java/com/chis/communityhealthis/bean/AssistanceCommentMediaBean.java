package com.chis.communityhealthis.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ASSISTANCE_COMMENT_MEDIA")
public class AssistanceCommentMediaBean implements Serializable {

    private static final long serialVersionUID = -7474362901041156597L;

    public static final String MEDIA_ID = "MEDIA_ID";
    public static final String ASSISTANCE_COMMENT_ID = "ASSISTANCE_COMMENT_ID";
    public static final String MEDIA_TYPE = "MEDIA_TYPE";
    public static final String MEDIA_NAME = "MEDIA_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = MEDIA_ID)
    private Integer mediaId;

    @Column(name = ASSISTANCE_COMMENT_ID)
    private Integer assistanceCommentId;

    @Column(name = MEDIA_TYPE)
    private String mediaType;

    @Column(name = MEDIA_NAME)
    private String mediaName;

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getAssistanceCommentId() {
        return assistanceCommentId;
    }

    public void setAssistanceCommentId(Integer commentId) {
        this.assistanceCommentId = commentId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }
}
