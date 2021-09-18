package com.chis.communityhealthis.controller;

import com.chis.communityhealthis.service.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(value = "/image")
public class ImageRestController {

    public static final String DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads";
    public static final String POST_MEDIA_DIRECTORY = DIRECTORY + "/post";

    @Autowired
    private ImageService imageService;

    @GetMapping(value = "/post/{postId}/{imageName:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public @ResponseBody
    byte[] getPostImg(@PathVariable Integer postId, @PathVariable(name = "imageName") String imageName) throws IOException {
        Path destination = Paths.get(POST_MEDIA_DIRECTORY + "/" + postId.toString() + "/" + imageName);
        return this.imageService.getImageWithMediaType(destination);
    }

}
