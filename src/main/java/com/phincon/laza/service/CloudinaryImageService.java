package com.phincon.laza.service;

import com.phincon.laza.model.dto.other.CloudinaryUploadResult;

public interface CloudinaryImageService {
  CloudinaryUploadResult upload(byte[] bytes, String folder, String fileId) throws Exception ;
}
