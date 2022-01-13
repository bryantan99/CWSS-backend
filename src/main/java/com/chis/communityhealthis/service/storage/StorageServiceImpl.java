package com.chis.communityhealthis.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.chis.communityhealthis.utility.DirectoryConstant;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public String uploadProfilePicture(String username, MultipartFile file) {
        File fileObj = toFile(file);
        String fileName = DirectoryConstant.AWS_ACCOUNT_PROFILE_PIC_DIRECTORY + "/" + username + "/" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return file.getOriginalFilename();
    }

    @Override
    public List<String> uploadPostMedias(Integer postId, List<MultipartFile> fileList) {
        List<String> fileNames = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileList)) {
            for (MultipartFile file : fileList) {
                File fileObj = toFile(file);
                String fileName = DirectoryConstant.AWS_POST_MEDIA_DIRECTORY + "/" + postId.toString() + "/" + file.getOriginalFilename();
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                fileObj.delete();
                fileNames.add(file.getOriginalFilename());
            }
        }
        return fileNames;
    }

    @Override
    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }

    @Override
    public void deleteFolderWithItsContents(String folderName) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderName);
        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);
        if (objectListing != null && !CollectionUtils.isEmpty(objectListing.getObjectSummaries())) {
            List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
            for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
                keys.add(new DeleteObjectsRequest.KeyVersion(summary.getKey()));
            }
            keys.add(new DeleteObjectsRequest.KeyVersion(folderName));
            DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucketName).withKeys(keys).withQuiet(false);
            s3Client.deleteObjects(multiObjectDeleteRequest);
        }
    }

    private File toFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }

}
