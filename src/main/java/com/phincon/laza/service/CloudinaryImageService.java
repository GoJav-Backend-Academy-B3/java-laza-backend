package com.phincon.laza.service;

import org.springframework.web.multipart.MultipartFile;

import com.phincon.laza.model.dto.cloudinary.CloudinaryUploadResult;

public interface CloudinaryImageService {
    CloudinaryUploadResult upload(byte[] bytes, String folder, String fileId) throws Exception;

    default CloudinaryUploadResult upload(MultipartFile file, String folder, String fileId) throws Exception {
        return upload(file.getBytes(), folder, fileId);
    };

    default CloudinaryUploadResult upload(MultipartFile file, String folder) throws Exception {
        return upload(file, folder, file.getResource().getFilename());
    };

    boolean delete(String publicId) throws Exception;
}
