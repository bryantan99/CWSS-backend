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
    private static final String ACTION_ACCEPT_HANDLE_ASSISTANCE = "Accepted to handle assistance. [assistanceId: %assistanceId%]";
    private static final String ACTION_CREATE_ASSISTANCE_REQUEST = "Created assistance request. [assistanceId: %assistanceId%]";
    private static final String ACTION_DELETE_ASSISTANCE_REQUEST = "Deleted assistance request. [assistanceId: %assistanceId%]";
    private static final String ACTION_UPDATE_ASSISTANCE_REQUEST = "Updated assistance request. [assistanceId: %assistanceId%]";
    private static final String ACTION_UPDATE_ASSISTANCE_STATUS = "Updated assistance status. [assistanceId: %assistanceId%]";
    private static final String ACTION_DELETE_ASSISTANCE_CATEGORY = "Deleted assistance's category. [categoryName: %categoryName%]";
    private static final String ACTION_CREATE_ASSISTANCE_CATEGORY = "Created assistance's category. [categoryName: %categoryName%]";
    private static final String ACTION_UPDATE_ASSISTANCE_CATEGORY = "Updated assistance's category. [categoryName: %categoryName%]";

    public static final String MODULE_APPOINTMENT = "APPOINTMENT";
    public static final String ACTION_RESCHEDULE_APPOINTMENT = "Rescheduled appointment. [appointmentId: %appointmentId%]";
    public static final String ACTION_ACCEPT_AND_RESCHEDULE_APPOINTMENT = "Accepted and rescheduled appointment. [appointmentId: %appointmentId%]";
    public static final String ACTION_CANCEL_APPOINTMENT = "Cancelled appointment. [appointmentId: %appointmentId%]";
    private static final String ACTION_CONFIRM_APPOINTMENT = "Confirmed appointment. [appointmentId: %appointmentId%]";
    private static final String ACTION_DELETE_APPOINTMENT = "Deleted appointment. [appointmentId: %appointmentId%]";
    private static final String ACTION_UPDATE_APPOINTMENT_STATUS = "Updated appointment's status. [appointmentId: %appointmentId%]";

    public static final String MODULE_ADMIN = "ADMIN";
    public static final String ACTION_UPDATE_ADMIN = "Update admin profile [username: %username%] [fullName: %fullName%]";
    public static final String ACTION_ADD_ADMIN = "Add new admin profile [username: %username%] [fullName: %fullName%]";
    public static final String ACTION_DELETE_ADMIN = "Delete admin profile [username: %username%] [fullName: %fullName%]";

    public static final String MODULE_COMMUNITY_USER = "COMMUNITY USER";
    public static final String ACTION_APPROVE_COMMUNITY_USER = "Approve community user's account [username: %username%] [fullName: %fullName%]";
    public static final String ACTION_REJECT_COMMUNITY_USER = "Reject community user's account [username: %username%] [fullName: %fullName%]";
    public static final String ACTION_DELETE_COMMUNITY_USER = "Delete community user's account [username: %username%] [fullName: %fullName%]";
    public static final String ACTION_UPDATE_COMMUNITY_USER = "Update community user's profile [username: %username%] [fullName: %fullName%]";
    private static final String ACTION_BLOCK_COMMUNITY_USER = "Block community user's account [username: %username%] [fullName: %fullName%]";
    private static final String ACTION_UNBLOCK_COMMUNITY_USER = "Unblock community user's account [username: %username%] [fullName: %fullName%]";

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

    public static String formatActionCancelAppointment(Integer appointmentId) {
        return StringUtils.replace(ACTION_CANCEL_APPOINTMENT, "%appointmentId%", appointmentId.toString());
    }

    public static String formatActionAcceptToHandleAssistance(Integer assistanceId) {
        return StringUtils.replace(ACTION_ACCEPT_HANDLE_ASSISTANCE, "%assistanceId%", assistanceId.toString());
    }

    public static String formatActionAcceptAndRescheduleAppointment(Integer appointmentId) {
        return StringUtils.replace(ACTION_ACCEPT_AND_RESCHEDULE_APPOINTMENT, "%appointmentId%", appointmentId.toString());
    }

    public static String formatActionRescheduleAppointment(Integer appointmentId) {
        return StringUtils.replace(ACTION_RESCHEDULE_APPOINTMENT, "%appointmentId%", appointmentId.toString());
    }

    public static String formatActionConfirmAppointment(Integer appointmentId) {
        return StringUtils.replace(ACTION_CONFIRM_APPOINTMENT, "%appointmentId%", appointmentId.toString());
    }

    public static String formatActionCreateAssistanceRequest(Integer assistanceId) {
        return StringUtils.replace(ACTION_CREATE_ASSISTANCE_REQUEST, "%assistanceId%", assistanceId.toString());
    }

    public static String formatActionDeleteAppointment(Integer appointmentId) {
        return StringUtils.replace(ACTION_DELETE_APPOINTMENT, "%appointmentId%", appointmentId.toString());
    }

    public static String formatActionDeleteAssistanceRequest(Integer assistanceId) {
        return StringUtils.replace(ACTION_DELETE_ASSISTANCE_REQUEST, "%assistanceId%", assistanceId.toString());
    }

    public static String formatActionUpdateAssistanceRequest(Integer assistanceId) {
        return StringUtils.replace(ACTION_UPDATE_ASSISTANCE_REQUEST, "%assistanceId%", assistanceId.toString());
    }

    public static String formatActionDeleteAssistanceCategory(String categoryName) {
        return StringUtils.replace(ACTION_DELETE_ASSISTANCE_CATEGORY, "%categoryName%", categoryName);
    }

    public static String formatActionCreateAssistanceCategory(String categoryName) {
        return StringUtils.replace(ACTION_CREATE_ASSISTANCE_CATEGORY, "%categoryName%", categoryName);
    }

    public static String formatActionUpdateAssistanceCategory(String categoryName) {
        return StringUtils.replace(ACTION_UPDATE_ASSISTANCE_CATEGORY, "%categoryName%", categoryName);
    }

    public static String formatActionBlockUser(String username, String fullName) {
        String replacedUsername = StringUtils.replace(ACTION_BLOCK_COMMUNITY_USER, "%username%", username);
        return StringUtils.replace(replacedUsername, "%fullName%", fullName);
    }

    public static String formatActionUnblockUser(String username, String fullName) {
        String replacedUsername = StringUtils.replace(ACTION_UNBLOCK_COMMUNITY_USER, "%username%", username);
        return StringUtils.replace(replacedUsername, "%fullName%", fullName);
    }

    public static String formatActionUpdateAssistanceStatus(Integer assistanceId) {
        return StringUtils.replace(ACTION_UPDATE_ASSISTANCE_STATUS, "%assistanceId%", assistanceId.toString());
    }

    public static String formatActionUpdateAppointmentStatus(Integer appointmentId) {
        return StringUtils.replace(ACTION_UPDATE_APPOINTMENT_STATUS, "%appointmentId%", appointmentId.toString());
    }
}
