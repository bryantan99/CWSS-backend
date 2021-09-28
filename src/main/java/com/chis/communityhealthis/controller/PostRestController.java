package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.bean.PostBean;
import com.chis.communityhealthis.model.PostForm;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.service.AuthService;
import com.chis.communityhealthis.service.PostService;
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

    @PostMapping
    public ResponseEntity<Object> addAdminPosts(@RequestParam(value = "form") String postForm,
                                                @RequestParam(value = "files", required = false) List<MultipartFile> multipartFileList) throws IOException {
        PostForm postFormObj = new ObjectMapper().readValue(postForm, PostForm.class);
        postFormObj.setCreatedBy(authService.getCurrentLoggedInUsername());
        postFormObj.setFileList(multipartFileList);

        try {
            PostBean postBean = postService.addPost(postFormObj);
            return ResponseHandler.generateResponse("Successfully added new post.", HttpStatus.OK, postBean);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Object> deleteAdminPost(@PathVariable Integer postId) {
        try {
            String actionMaker = authService.getCurrentLoggedInUsername();
            postService.deletePost(postId, actionMaker);
            return ResponseHandler.generateResponse("Successfully deleted post ID: " + postId.toString() + ".", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> updatePost(@RequestParam(value = "form") String postForm,
                                        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFileList) throws IOException {
        PostForm postFormObj = new ObjectMapper().readValue(postForm, PostForm.class);
        postFormObj.setCreatedBy(authService.getCurrentLoggedInUsername());
        postFormObj.setFileList(multipartFileList);

        try {
            PostBean updatedPostBean = postService.updatePost(postFormObj);
            return ResponseHandler.generateResponse("Successfully updated post ID: " + postFormObj.getPostId() + ".", HttpStatus.OK, updatedPostBean);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
