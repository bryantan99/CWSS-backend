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
public class AdminPostRestController {

    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/get-post", method = RequestMethod.GET)
    public ResponseEntity<PostBean> getPostById(@RequestParam Integer postId) {
        return new ResponseEntity<>(postService.getPostWithMedia(postId), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-admin-posts", method = RequestMethod.GET)
    public ResponseEntity<List<PostBean>> getAdminPosts() {
        return new ResponseEntity<>(postService.getPostsWithMedia(), HttpStatus.OK);
    }

    @PostMapping(value = "/add-admin-post")
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
            postService.deletePost(postId);
            return ResponseHandler.generateResponse("Successfully deleted post ID: " + postId.toString() + ".", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @RequestMapping(value = "/update-post", method = RequestMethod.POST)
    public ResponseEntity<?> updatePost(@RequestParam(value = "form") String postForm, @RequestParam(value = "files", required = false) List<MultipartFile> multipartFileList) throws IOException {
        PostForm postFormObj = new ObjectMapper().readValue(postForm, PostForm.class);
        postFormObj.setCreatedBy(authService.getCurrentLoggedInUsername());
        postFormObj.setFileList(multipartFileList);
        return new ResponseEntity<>(postService.updatePost(postFormObj), HttpStatus.OK);
    }
}
