package com.chis.communityhealthis.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "POST")
public class PostBean implements Serializable {

    private static final long serialVersionUID = 38106780748088157L;

    public static final String POST_ID = "POST_ID";
    public static final String POST_DESCRIPTION = "POST_DESCRIPTION";
    public static final String CREATED_BY = "CREATED_BY";
    public static final String CREATED_DATE = "CREATED_DATE";

    @Id
    @Column(name = POST_ID, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(name = POST_DESCRIPTION)
    private String postDescription;

    @Column(name = CREATED_BY)
    private String createdBy;

    @Column(name = CREATED_DATE)
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = CREATED_BY,referencedColumnName = AdminBean.USERNAME, updatable = false, insertable = false)
    private AdminBean adminBean;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public AdminBean getAdminBean() { return adminBean; }

    public void setAdminBean(AdminBean adminBean) { this.adminBean = adminBean; }
}
