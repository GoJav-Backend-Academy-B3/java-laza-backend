package com.phincon.laza.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.phincon.laza.model.dto.other.CloudinaryUploadResult;
import com.phincon.laza.service.CloudinaryImageService;

@Service
@Qualifier("fake")
public class FakeCloudinaryImageServiceImpl implements CloudinaryImageService {

    private Path tempDirectory;

    public FakeCloudinaryImageServiceImpl() {
        try {
            tempDirectory = Files.createTempDirectory("lmao");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CloudinaryUploadResult upload(byte[] bytes, String folder, String fileId) throws Exception {
        Path f = Files.write(tempDirectory.resolve(fileId), bytes, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
        return new CloudinaryUploadResult(fileId, 0, 0, "", bytes.length, f.toString());
    }

    @Override
    public boolean delete(String publicId) throws Exception {
        try {
            Files.delete(tempDirectory.resolve(publicId));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
