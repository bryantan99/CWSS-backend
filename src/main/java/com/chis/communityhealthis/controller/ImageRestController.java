package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.service.storage.StorageService;
import com.chis.communityhealthis.utility.DirectoryConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/image")
public class ImageRestController {

    @Autowired
    private StorageService storageService;

    @GetMapping(value = "/post/{postId}/{imageName:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public @ResponseBody
    byte[] getPostImg(@PathVariable Integer postId, @PathVariable(name = "imageName") String imageName) {
        final String relativeToPostImg = DirectoryConstant.AWS_POST_MEDIA_DIRECTORY + "/" + postId.toString() + "/" + imageName;
        return this.storageService.downloadFile(relativeToPostImg);
    }

    @GetMapping(value = "/account/profile-pic/{username}/{imageName:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody
    byte[] getProfilePic(@PathVariable String username, @PathVariable(name = "imageName") String imageName) {
        final String DEFAULT_IMG = "profile_pic_placeholder.jpg";
        final String relativeUrlToDefaultProfilePic = DirectoryConstant.AWS_ACCOUNT_PROFILE_PIC_DIRECTORY + "/" + DEFAULT_IMG;
        String relativeUrlToImg = DirectoryConstant.AWS_ACCOUNT_PROFILE_PIC_DIRECTORY + "/" + username + "/" + imageName;

        try {
            return storageService.downloadFile(relativeUrlToImg);
        } catch (Exception e) {
            return storageService.downloadFile(relativeUrlToDefaultProfilePic);
        }
    }

}
