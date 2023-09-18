package com.phincon.laza.service;

import com.phincon.laza.model.dto.cloudinary.CloudinaryUploadResult;
import com.phincon.laza.model.entity.PaymentMethod;
import com.phincon.laza.repository.PaymentMethodRepository;
import com.phincon.laza.service.impl.CloudinaryImageServiceImpl;
import com.phincon.laza.service.impl.PaymentMethodServiceImpl;
import com.phincon.laza.validator.PaymentMethodValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class PaymentMethodServiceImplTest {

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private PaymentMethodValidator paymentMethodValidator;

    @Mock
    private CloudinaryImageServiceImpl cloudinaryImageService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mock data
        PaymentMethod paymentMethod1 = new PaymentMethod();
        paymentMethod1.setId(1L);
        paymentMethod1.setName("Sample Payment Method");
        paymentMethod1.setCode("SAMPLE_CODE");
        paymentMethod1.setType("Sample Type");
        paymentMethod1.setProvider("Sample Provider");
        paymentMethod1.setAdminFee(10);
        paymentMethod1.setIsActive(true);
        paymentMethod1.setLogoUrl("https://example.com/logo.png");

        PaymentMethod paymentMethod2 = new PaymentMethod();
        paymentMethod2.setId(2L);
        paymentMethod2.setName("Sample Payment Method 2");
        paymentMethod2.setCode("SAMPLE_CODE 2");
        paymentMethod2.setType("Sample Type 2");
        paymentMethod2.setProvider("Sample Provider 2");
        paymentMethod2.setAdminFee(20);
        paymentMethod2.setIsActive(false);
        paymentMethod2.setLogoUrl("https://example.com/logo2.png");

        PaymentMethod updatedPaymentMethod = new PaymentMethod();
        updatedPaymentMethod.setId(99L);
        updatedPaymentMethod.setName("update");
        updatedPaymentMethod.setCode("update");
        updatedPaymentMethod.setType("update");
        updatedPaymentMethod.setProvider("update");
        updatedPaymentMethod.setAdminFee(99);
        updatedPaymentMethod.setIsActive(false);
        updatedPaymentMethod.setLogoUrl("update");

        List<PaymentMethod> paymentMethods = Arrays.asList(paymentMethod1, paymentMethod2);

        lenient().when(paymentMethodRepository.findAll()).thenReturn(paymentMethods);
        lenient().when(paymentMethodRepository.findAllByIsActiveIsTrue()).thenReturn(List.of(paymentMethod1));

        lenient().when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(paymentMethod1));
        lenient().when(paymentMethodRepository.findById(2L)).thenReturn(Optional.of(paymentMethod2));

        lenient().when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod1);

        Optional<PaymentMethod> optionalUpdatePaymentMethod = Optional.of(updatedPaymentMethod);
        lenient().when(paymentMethodRepository.findById(99L)).thenReturn(optionalUpdatePaymentMethod);
        lenient().when(paymentMethodRepository.save(updatedPaymentMethod)).thenReturn(updatedPaymentMethod);
        lenient().when(paymentMethodRepository.findTop1ByNameAndIsActiveIsTrue("1")).thenReturn(Optional.of(paymentMethod1));
        lenient().when(paymentMethodRepository.findTop1ByNameAndIsActiveIsTrue("2")).thenReturn(Optional.empty());

        // Create a byte array to represent a simple PNG image
        byte[] mockImageData = new byte[] {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', (byte) 0x1A, '\n'};
        MockMultipartFile logo = new MockMultipartFile(
                "logo",
                "logo.png",
                MediaType.IMAGE_PNG_VALUE,
                mockImageData
        );

        CloudinaryUploadResult uploadResult = new CloudinaryUploadResult("publidId", 1, 1, "format", 1, "secureUrl");
        lenient().when(cloudinaryImageService.upload(any(MultipartFile.class), anyString(), anyString())).thenReturn(uploadResult);
    }

    @Test
    void testGetAllPaymentMethods() {

        // Act
        List<PaymentMethod> result = paymentMethodService.getAllPaymentMethods();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(paymentMethodRepository, times(1)).findAll();
    }

    @Test
    void testGetAllActivePaymentMethods() {
        // Act
        List<PaymentMethod> result = paymentMethodService.getAllActivePaymentMethods();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Sample Payment Method", result.get(0).getName());
        assertEquals("SAMPLE_CODE", result.get(0).getCode());
        assertEquals(true, result.get(0).getIsActive());

        verify(paymentMethodRepository, times(1)).findAllByIsActiveIsTrue();
    }

    @Test
    void testGetPaymentMethodById() {
        // Act
        PaymentMethod result = paymentMethodService.getPaymentMethodById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Sample Payment Method", result.getName());
        assertEquals("SAMPLE_CODE", result.getCode());
        assertEquals(true, result.getIsActive());

        verify(paymentMethodRepository, times(1)).findById(1L);
    }

    @Test
    void testCreatePaymentMethod() {
        // Mock data
        PaymentMethod paymentMethod1 = new PaymentMethod();
        paymentMethod1.setId(1L);
        paymentMethod1.setName("Sample Payment Method");
        paymentMethod1.setCode("SAMPLE_CODE");
        paymentMethod1.setType("Sample Type");
        paymentMethod1.setProvider("Sample Provider");
        paymentMethod1.setAdminFee(10);
        paymentMethod1.setIsActive(true);
        paymentMethod1.setLogoUrl("https://example.com/logo.png");

        // Act & Assert
        PaymentMethod result = paymentMethodService.createPaymentMethod(paymentMethod1);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getId());
        assertEquals("Sample Payment Method", result.getName());
        assertEquals("SAMPLE_CODE", result.getCode());
        assertEquals(true, result.getIsActive());

        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void testUpdatePaymentMethod() {
        // Arrange
        PaymentMethod updatedPaymentMethod = new PaymentMethod();
        updatedPaymentMethod.setId(99L);
        updatedPaymentMethod.setName("update");
        updatedPaymentMethod.setCode("update");
        updatedPaymentMethod.setType("update");
        updatedPaymentMethod.setProvider("update");
        updatedPaymentMethod.setAdminFee(99);
        updatedPaymentMethod.setIsActive(false);
        updatedPaymentMethod.setLogoUrl("update");

        // Act
        PaymentMethod result = paymentMethodService.updatePaymentMethod(99L, updatedPaymentMethod);

        // Assert
        assertNotNull(result);
        assertEquals(99L, result.getId());
        assertEquals("update", result.getName());
        assertEquals("update", result.getCode());
        assertEquals(false, result.getIsActive());

        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void testActivatePaymentMethod() {
        // Arrange
        Long id = 2L;
        PaymentMethod existingPaymentMethod = new PaymentMethod();
        existingPaymentMethod.setName("2");
        existingPaymentMethod.setId(id);
        existingPaymentMethod.setIsActive(true);

        when(paymentMethodRepository.findById(id)).thenReturn(Optional.of(existingPaymentMethod));

        PaymentMethod result = paymentMethodService.activatePaymentMethod(2L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(true, result.getIsActive());

        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void testDeactivatePaymentMethod() {
        // Arrange
        Long id = 1L;
        PaymentMethod existingPaymentMethod = new PaymentMethod();
        existingPaymentMethod.setName("1");
        existingPaymentMethod.setId(id);
        existingPaymentMethod.setIsActive(false);

        when(paymentMethodRepository.findById(id)).thenReturn(Optional.of(existingPaymentMethod));

        PaymentMethod result = paymentMethodService.deactivatePaymentMethod(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(true, result.getIsActive());

        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void testUpdatePaymentMethodLogo() {
        // Arrange
        PaymentMethod existingPaymentMethod = new PaymentMethod();
        existingPaymentMethod.setId(1L);

        // Create a byte array to represent a simple PNG image
        byte[] mockImageData = new byte[] {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', (byte) 0x1A, '\n'};

        // Create a MockMultipartFile with the image data
        MockMultipartFile logo = new MockMultipartFile(
                "logo",
                "logo.png",
                MediaType.IMAGE_PNG_VALUE,
                mockImageData
        );

        // Act
        PaymentMethod result = paymentMethodService.updatePaymentMethodLogo(1L, logo);

        // Assert
        assertNotNull(result);
        // Add more assertions for the updatedPaymentMethod here
    }

    // Add more test methods for other service methods

    // Ensure to test different scenarios, including error cases
}

