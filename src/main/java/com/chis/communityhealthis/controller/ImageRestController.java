package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.service.image.ImageService;
import com.chis.communityhealthis.utility.DirectoryConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(value = "/image")
public class ImageRestController {

    @Autowired
    private ImageService imageService;

    @GetMapping(value = "/post/{postId}/{imageName:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public @ResponseBody
    byte[] getPostImg(@PathVariable Integer postId, @PathVariable(name = "imageName") String imageName) throws IOException {
        Path destination = Paths.get(DirectoryConstant.POST_MEDIA_DIRECTORY + "/" + postId.toString() + "/" + imageName);
        return this.imageService.getImageWithMediaType(destination);
    }

    @GetMapping(value = "/account/profile-pic/{username}/{imageName:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody
    byte[] getProfilePic(@PathVariable String username, @PathVariable(name = "imageName") String imageName) throws IOException {
        final String DEFAULT_IMG = "profile_pic_placeholder.jpg";
        try {
            Path destination = Paths.get(DirectoryConstant.ACCOUNT_PROFILE_PIC_DIRECTORY + "/" + username + "/" + imageName);
            return this.imageService.getImageWithMediaType(destination);
        } catch (Exception e) {
            Path destination = Paths.get(DirectoryConstant.ACCOUNT_PROFILE_PIC_DIRECTORY + "/" + DEFAULT_IMG);
            return this.imageService.getImageWithMediaType(destination);
        }
    }

}
