package com.chis.communityhealthis.service.image;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageService {
    byte[] getImageWithMediaType(Path imgPath) throws IOException;
}
