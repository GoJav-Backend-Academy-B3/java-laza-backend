package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class FileValidatorTest {
    @InjectMocks
    private FileValidator fileValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateFileJpg_thenCorrect() {
        MultipartFile file = new MockMultipartFile(
                "photo.jpg",
                "photo.jpg",
                "image/jpg",
                new byte[1024 * 1024]
        );

        fileValidator.validateMultipartFile(file);

        log.info("[COMPLETE] testing validate file jpg then correct");
    }

    @Test
    public void testValidateFileJpeg_thenCorrect() {
        MultipartFile file = new MockMultipartFile(
                "photo.jpeg",
                "photo.jpeg",
                "image/jpeg",
                new byte[1024 * 1024]
        );

        fileValidator.validateMultipartFile(file);

        log.info("[COMPLETE] testing validate file jpeg then correct");
    }

    @Test
    public void testValidateFilePng_thenCorrect() {
        MultipartFile file = new MockMultipartFile(
                "photo.png",
                "photo.png",
                "image/png",
                new byte[1024 * 1024]
        );

        fileValidator.validateMultipartFile(file);

        log.info("[COMPLETE] testing validate file png then correct");
    }

    @Test
    public void testValidateFileWebp_thenCorrect() {
        MultipartFile file = new MockMultipartFile(
                "photo.webp",
                "photo.webp",
                "image/webp",
                new byte[1024 * 1024]
        );

        fileValidator.validateMultipartFile(file);

        log.info("[COMPLETE] testing validate file webp then correct");
    }


    @Test
    public void testValidateFile_thenInvalidContentTypeFile() {
        MultipartFile file = new MockMultipartFile(
                "document.pdf",
                "document.pdf",
                "application/pdf",
                new byte[1024 * 1024]
        );

        assertThrows(BadRequestException.class, () -> {
            fileValidator.validateMultipartFile(file);
        });

        log.info("[COMPLETE] testing validate file pdf then invalid content type file");
    }

    @Test
    public void testValidateFile_thenMaxFileSize() {
        MultipartFile file = new MockMultipartFile(
                "photo.jpg",
                "photo.jpg",
                "image/jpg",
                new byte[3 * 1024 * 1024]
        );

        assertThrows(MaxUploadSizeExceededException.class, () -> {
            fileValidator.validateMultipartFile(file);
        });

        log.info("[COMPLETE] testing validate file jpg then max file size");
    }
}
