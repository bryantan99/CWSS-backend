package com.chis.communityhealthis.bean;

import org.javers.core.metamodel.annotation.TypeName;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "POST_MEDIA")
@TypeName("PostMediaBean")
public class PostMediaBean implements Serializable {

    private static final long serialVersionUID = -8900827186653454131L;

    public static final String MEDIA_ID = "MEDIA_ID";
    public static final String POST_ID = "POST_ID";
    public static final String MEDIA_TYPE = "MEDIA_TYPE";
    public static final String MEDIA_DIRECTORY = "MEDIA_DIRECTORY";

    @Id
    @Column(name = MEDIA_ID, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mediaId;

    @Column(name = POST_ID)
    private Integer postId;

    @Column(name = MEDIA_TYPE)
    private String mediaType;

    @Column(name = MEDIA_DIRECTORY)
    private String mediaDirectory;

    public PostMediaBean() {
    }

    public PostMediaBean(Integer mediaId, Integer postId, String mediaType, String mediaDirectory) {
        this.mediaId = mediaId;
        this.postId = postId;
        this.mediaType = mediaType;
        this.mediaDirectory = mediaDirectory;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaDirectory() {
        return mediaDirectory;
    }

    public void setMediaDirectory(String mediaDirectory) {
        this.mediaDirectory = mediaDirectory;
    }
}
