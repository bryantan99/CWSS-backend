package com.chis.communityhealthis.bean;

import org.javers.core.metamodel.annotation.TypeName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "POST")
@TypeName("PostBean")
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

    @OneToMany
    @JoinColumn(name = POST_ID, referencedColumnName = PostMediaBean.POST_ID, updatable = false, insertable = false)
    private Set<PostMediaBean> postMediaBeanSet;

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

    public Set<PostMediaBean> getPostMediaBeanSet() {
        return postMediaBeanSet;
    }

    public void setPostMediaBeanSet(Set<PostMediaBean> postMediaBeanSet) {
        this.postMediaBeanSet = postMediaBeanSet;
    }
}
