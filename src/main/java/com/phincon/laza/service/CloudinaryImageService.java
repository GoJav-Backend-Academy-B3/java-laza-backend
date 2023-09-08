package com.phincon.laza.service;

import org.springframework.web.multipart.MultipartFile;

import com.phincon.laza.model.dto.other.CloudinaryUploadResult;

public interface CloudinaryImageService {
    CloudinaryUploadResult upload(byte[] bytes, String folder, String fileId) throws Exception;

    CloudinaryUploadResult upload(MultipartFile file, String folder, String fileId) throws Exception;

    CloudinaryUploadResult upload(MultipartFile file, String folder) throws Exception;
}
