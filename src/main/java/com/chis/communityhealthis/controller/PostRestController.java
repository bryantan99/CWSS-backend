package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.bean.PostBean;
import com.chis.communityhealthis.model.post.PostForm;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.model.user.LoggedInUser;
import com.chis.communityhealthis.service.auth.AuthService;
import com.chis.communityhealthis.service.post.PostService;
import com.chis.communityhealthis.utility.RoleConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostRestController {

    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    @GetMapping(value = "/{postId}")
    public ResponseEntity<Object> getPostById(@PathVariable Integer postId) {
        try {
            PostBean postBean = postService.getPostWithMedia(postId);
            return ResponseHandler.generateResponse("Successfully retrieved post with ID: " + postId.toString() + ".", HttpStatus.OK, postBean);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getPosts() {
        try {
            List<PostBean> list = postService.getPostsWithMedia();
            return ResponseHandler.generateResponse("Successfully retrieved " + list.size() + " post(s).", HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/new")
    public ResponseEntity<Object> addAdminPosts(@RequestParam(value = "form") String postForm,
                                                @RequestParam(value = "files", required = false) List<MultipartFile> multipartFileList) {
        try {
            PostForm postFormObj = initPostForm(postForm, multipartFileList);
            PostBean postBean = postService.addPost(postFormObj);
            return ResponseHandler.generateResponse("Successfully added new post.", HttpStatus.OK, postBean);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Object> deleteAdminPost(@PathVariable Integer postId) {
        try {
            LoggedInUser user = authService.getCurrentLoggedInUser();
            if (!user.getRoleList().contains(RoleConstant.ADMIN) && !user.getRoleList().contains(RoleConstant.SUPER_ADMIN)) {
                throw new Exception("Unauthorized user.");
            }
            postService.deletePost(postId, user.getUsername());
            return ResponseHandler.generateResponse("Successfully deleted post ID: " + postId.toString() + ".", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> updatePost(@RequestParam(value = "form") String postForm,
                                        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFileList) {
        try {
            PostForm postFormObj = initPostForm(postForm, multipartFileList);
            PostBean updatedPostBean = postService.updatePost(postFormObj);
            return ResponseHandler.generateResponse("Successfully updated post ID: " + postFormObj.getPostId() + ".", HttpStatus.OK, updatedPostBean);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    private PostForm initPostForm(String postForm, List<MultipartFile> multipartFileList) throws Exception {
        LoggedInUser user = authService.getCurrentLoggedInUser();
        if (!user.getRoleList().contains(RoleConstant.ADMIN) && !user.getRoleList().contains(RoleConstant.SUPER_ADMIN)) {
            throw new Exception("Unauthorized user.");
        }

        PostForm postFormObj = new ObjectMapper().readValue(postForm, PostForm.class);
        postFormObj.setCreatedBy(user.getUsername());
        postFormObj.setFileList(multipartFileList);
        return postFormObj;
    }
}
