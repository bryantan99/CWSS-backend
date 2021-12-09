package com.chis.communityhealthis.utility;

import java.util.Arrays;
import java.util.List;

public class AuditConstant {

    public static final String MODULE_POST = "POST";
    public static final String ACTION_CREATE_POST = "Create post [postId: %postId%]";
    public static final String ACTION_UPDATE_POST = "Update post [postId: %postId%]";
    public static final String ACTION_DELETE_POST = "Delete post [postId: %postId%]";
    public static final String ACTION_CREATE_POST_MEDIA = "Create post media [postMediaId: %postMediaId%]";
    public static final String ACTION_DELETE_POST_MEDIA = "Delete post media [postMediaId: %postMediaId%]";

    public static final String MODULE_ASSISTANCE = "ASSISTANCE";
    public static final String MODULE_APPOINTMENT = "APPOINTMENT";

    public static final String MODULE_ADMIN = "ADMIN";
    public static final String ACTION_UPDATE_ADMIN = "Update admin [username: %username%]";
    public static final List<String> MODULE_LIST = Arrays.asList(MODULE_POST, MODULE_ADMIN, MODULE_APPOINTMENT, MODULE_ASSISTANCE);
}
