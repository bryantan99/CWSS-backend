package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.bean.AssistanceCommentBean;
import com.chis.communityhealthis.model.assistanceComment.AssistanceCommentForm;
import com.chis.communityhealthis.model.assistanceComment.AssistanceCommentModel;
import com.chis.communityhealthis.model.response.ResponseHandler;
import com.chis.communityhealthis.service.AuthService;
import com.chis.communityhealthis.service.assistanceComment.AssistanceCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> addAssistanceComment(@RequestBody AssistanceCommentForm form) {
        try {
            form.setCreatedBy(authService.getCurrentLoggedInUsername());
            form.setCreatedDate(new Date());
            AssistanceCommentBean bean = assistanceCommentService.addComment(form);
            return ResponseHandler.generateResponse("Successfully added new comment for assistance ID: " + form.getAssistanceId().toString() + ".", HttpStatus.OK, bean);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
