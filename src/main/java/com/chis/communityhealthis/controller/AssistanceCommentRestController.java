package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.bean.AssistanceCommentBean;
import com.chis.communityhealthis.model.assistancecomment.AssistanceCommentForm;
import com.chis.communityhealthis.model.assistancecomment.AssistanceCommentModel;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.service.assistancecomment.AssistanceCommentService;
import com.chis.communityhealthis.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/assistance")
public class AssistanceCommentRestController {

    @Autowired
    private AssistanceCommentService assistanceCommentService;

    @Autowired
    private AuthService authService;

    @GetMapping(value = "/{assistanceId}/comment")
    public ResponseEntity<Object> findAssistanceComments(@PathVariable Integer assistanceId) {
        try {
            List<AssistanceCommentModel> list = assistanceCommentService.findComments(assistanceId);
            String respMsg = "Successfully retrieved " + list.size() + " comment(s) for assistance record ID : " + assistanceId.toString() + ".";
            return ResponseHandler.generateResponse(respMsg, HttpStatus.OK, list);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<Object> addAssistanceComment(@RequestParam(value = "form") String form,
                                                       @RequestParam(value = "files", required = false) List<MultipartFile> multipartFileList) {
        try {
            AssistanceCommentForm assistanceCommentForm = new ObjectMapper().readValue(form, AssistanceCommentForm.class);
            assistanceCommentForm.setCreatedBy(authService.getCurrentLoggedInUsername());
            assistanceCommentForm.setCreatedDate(new Date());
            if (!CollectionUtils.isEmpty(multipartFileList)) {
                assistanceCommentForm.setFileList(multipartFileList);
            }
            AssistanceCommentBean bean = assistanceCommentService.addComment(assistanceCommentForm);
            return ResponseHandler.generateResponse("Successfully added new comment for assistance ID: " + assistanceCommentForm.getAssistanceId().toString() + ".", HttpStatus.OK, bean);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
