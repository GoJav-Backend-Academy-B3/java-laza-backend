package com.phincon.laza.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.phincon.laza.model.dto.other.CloudinaryUploadResult;
import com.phincon.laza.service.CloudinaryImageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CloudinaryImageServiceImpl implements CloudinaryImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Value("${com.phincon.laza.cloudinary.folder-prefix}")
    private String folderPrefix;

    @Override
    public CloudinaryUploadResult upload(byte[] bytes, String folder, String fileId) throws Exception {
        var uploader = cloudinary.uploader();
        try {
            var result = uploader.upload(bytes,
                    ObjectUtils.asMap(
                            "folder", String.format("%s/%s", folderPrefix, folder),
                            "public_id", fileId));
            return CloudinaryUploadResult.fromMap(result);
        } catch (IOException e) {
            log.error("Cannot upload file. Reason {}", e.getMessage());
            throw e;
        }
    }
}
