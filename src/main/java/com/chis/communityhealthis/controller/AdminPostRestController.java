package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.model.PostForm;
import com.chis.communityhealthis.service.AuthService;
import com.chis.communityhealthis.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class AdminPostRestController {

    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/get-admin-posts", method = RequestMethod.GET)
    public ResponseEntity<?> getAdminPosts() {
        return new ResponseEntity<>(postService.getPosts(), HttpStatus.OK);
    }

    @RequestMapping(value = "/add-admin-post", method = RequestMethod.POST)
    public ResponseEntity<?> addAdminPosts(@RequestBody PostForm postForm) {
        postForm.setCreatedBy(authService.getCurrentLoggedInUsername());
        return new ResponseEntity<>(postService.addPost(postForm), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete-admin-post/{postId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAdminPost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
