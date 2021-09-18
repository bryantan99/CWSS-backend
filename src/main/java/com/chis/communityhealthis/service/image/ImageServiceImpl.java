package com.chis.communityhealthis.service.image;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public byte[] getImageWithMediaType(Path imgPath) throws IOException {
        return IOUtils.toByteArray(imgPath.toUri());
    }

}
