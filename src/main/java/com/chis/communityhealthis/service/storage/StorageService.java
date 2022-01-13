package com.chis.communityhealthis.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    String uploadProfilePicture(String username, MultipartFile file);
    List<String> uploadPostMedias(Integer postId, List<MultipartFile> fileList);
    byte[] downloadFile(String fileName);
    String deleteFile(String fileName);
    void deleteFolderWithItsContents(String folderName);
}
