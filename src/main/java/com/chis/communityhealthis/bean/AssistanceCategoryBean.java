package com.chis.communityhealthis.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ASSISTANCE_CATEGORY")
public class AssistanceCategoryBean implements Serializable {

    private static final long serialVersionUID = -6770724871607080753L;

    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = CATEGORY_ID)
    private Integer categoryId;

    @Column(name = CATEGORY_NAME)
    private String categoryName;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
