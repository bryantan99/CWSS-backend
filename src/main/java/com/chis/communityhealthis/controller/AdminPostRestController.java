package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.bean.PostBean;
import com.chis.communityhealthis.model.PostForm;
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

    @RequestMapping(value = "/get-admin-posts", method = RequestMethod.GET)
    public ResponseEntity<List<PostBean>> getAdminPosts() {
        return new ResponseEntity<>(postService.getPostsWithMedia(), HttpStatus.OK);
    }

    @RequestMapping(value = "/add-admin-post", method = RequestMethod.POST)
    public ResponseEntity<?> addAdminPosts(@RequestParam(value = "form") String postForm, @RequestParam(value = "files", required = false) List<MultipartFile> multipartFileList) throws IOException {
        PostForm postFormObj = new ObjectMapper().readValue(postForm, PostForm.class);
        postFormObj.setCreatedBy(authService.getCurrentLoggedInUsername());
        postFormObj.setFileList(multipartFileList);
        return new ResponseEntity<>(postService.addPost(postFormObj), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete-admin-post/{postId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAdminPost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
