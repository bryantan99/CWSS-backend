package com.chis.communityhealthis.utility;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class AuditConstant {

    public static final String MODULE_POST = "POST";
    public static final String ACTION_CREATE_POST = "Create post [postId: %postId%]";
    public static final String ACTION_UPDATE_POST = "Update post [postId: %postId%]";
    public static final String ACTION_DELETE_POST = "Delete post [postId: %postId%]";
    public static final String ACTION_CREATE_POST_MEDIA = "Create post media [postMediaId: %postMediaId%, mediaDirectory: %mediaDirectory%]";
    public static final String ACTION_DELETE_POST_MEDIA = "Delete post media [postMediaId: %postMediaId%, mediaDirectory: %mediaDirectory%]";

    public static final String MODULE_ASSISTANCE = "ASSISTANCE";
    public static final String MODULE_APPOINTMENT = "APPOINTMENT";

    public static final String MODULE_ADMIN = "ADMIN";
    public static final String ACTION_UPDATE_ADMIN = "Update admin profile [username: %username%] [fullName: %fullName%]";
    public static final String ACTION_ADD_ADMIN = "Add new admin profile [username: %username%] [fullName: %fullName%]";
    public static final String ACTION_DELETE_ADMIN = "Delete admin profile [username: %username%] [fullName: %fullName%]";

    public static final String MODULE_COMMUNITY_USER = "COMMUNITY USER";
    public static final String ACTION_APPROVE_COMMUNITY_USER = "Approve community user's account [username: %username%] [fullName: %fullName%]";
    public static final String ACTION_REJECT_COMMUNITY_USER = "Reject community user's account [username: %username%] [fullName: %fullName%]";
    public static final String ACTION_DELETE_COMMUNITY_USER = "Delete community user's account [username: %username%] [fullName: %fullName%]";
    public static final String ACTION_UPDATE_COMMUNITY_USER = "Update community user's profile [username: %username%] [fullName: %fullName%]";

    public static final List<String> MODULE_LIST = Arrays.asList(MODULE_POST, MODULE_ADMIN, MODULE_APPOINTMENT, MODULE_ASSISTANCE, MODULE_COMMUNITY_USER);

    public static String formatActionAddAdmin(String username, String fullName) {
        String replacedUsername = StringUtils.replace(ACTION_ADD_ADMIN, "%username%", username);
        return StringUtils.replace(replacedUsername, "%fullName%", fullName);
    }

    public static String formatActionUpdateAdmin(String username, String fullName) {
        String replacedUsername = StringUtils.replace(ACTION_UPDATE_ADMIN, "%username%", username);
        return StringUtils.replace(replacedUsername, "%fullName%", fullName);
    }

    public static String formatActionDeleteAdmin(String username, String fullName) {
        String replacedUsername = StringUtils.replace(ACTION_DELETE_ADMIN, "%username%", username);
        return StringUtils.replace(replacedUsername, "%fullName%", fullName);
    }

    public static String formatActionApproveCommunityUser(String username, String fullName) {
        String replacedUsername = StringUtils.replace(ACTION_APPROVE_COMMUNITY_USER, "%username%", username);
        return StringUtils.replace(replacedUsername, "%fullName%", fullName);
    }

    public static String formatActionRejectCommunityUser(String username, String fullName) {
        String replacedUsername = StringUtils.replace(ACTION_REJECT_COMMUNITY_USER, "%username%", username);
        return StringUtils.replace(replacedUsername, "%fullName%", fullName);
    }

    public static String formatActionDeleteCommunityUser(String username, String fullName) {
        String replacedUsername = StringUtils.replace(ACTION_DELETE_COMMUNITY_USER, "%username%", username);
        return StringUtils.replace(replacedUsername, "%fullName%", fullName);
    }

    public static String formatActionUpdateCommunityUser(String username, String fullName) {
        String replacedUsername = StringUtils.replace(ACTION_UPDATE_COMMUNITY_USER, "%username%", username);
        return StringUtils.replace(replacedUsername, "%fullName%", fullName);
    }
}
