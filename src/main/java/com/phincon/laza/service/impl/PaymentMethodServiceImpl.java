package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.model.dto.cloudinary.CloudinaryUploadResult;
import com.phincon.laza.model.entity.PaymentMethod;
import com.phincon.laza.repository.PaymentMethodRepository;
import com.phincon.laza.service.PaymentMethodService;
import com.phincon.laza.validator.PaymentMethodValidator;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodValidator paymentMethodValidator;

    private final CloudinaryImageServiceImpl cloudinaryImageService;

    @Autowired
    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository, PaymentMethodValidator paymentMethodValidator, CloudinaryImageServiceImpl cloudinaryImageService) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodValidator = paymentMethodValidator;
        this.cloudinaryImageService = cloudinaryImageService;
    }

    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public List<PaymentMethod> getAllActivePaymentMethods() {
        return paymentMethodRepository.findAllByIsActiveIsTrue();
    }

    public PaymentMethod getPaymentMethodById(Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);

        paymentMethodValidator.validatePaymentMethodNotFound(paymentMethod, id);

        return paymentMethod.get();
    }

    public PaymentMethod createPaymentMethod(PaymentMethod paymentMethod) {
        try {
            paymentMethod.setIsActive(false);
            return paymentMethodRepository.save(paymentMethod);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public PaymentMethod updatePaymentMethod(Long id, PaymentMethod updatedPaymentMethod) {
        try {
            Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
            paymentMethodValidator.validatePaymentMethodNotFound(paymentMethod, id);

            updatedPaymentMethod.setId(id);
            return paymentMethodRepository.save(updatedPaymentMethod);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentMethod updatePaymentMethodLogo(Long id, MultipartFile logo) {
        try {
            Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
            paymentMethodValidator.validatePaymentMethodNotFound(paymentMethod, id);

            PaymentMethod updatedPaymentMethod = paymentMethod.get();

            CloudinaryUploadResult uploadResult = cloudinaryImageService.upload(logo, "payment-method", GenerateLogoFileName(updatedPaymentMethod));
            updatedPaymentMethod.setLogoUrl(uploadResult.secureUrl());
            return updatedPaymentMethod;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePaymentMethod(Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        paymentMethodValidator.validatePaymentMethodNotFound(paymentMethod, id);
        paymentMethodRepository.deleteById(id);
    }

    @Override
    public PaymentMethod deactivatePaymentMethod(Long id) {
        PaymentMethod paymentMethod = getPaymentMethodById(id);
        paymentMethod.setIsActive(false);
        return updatePaymentMethod(paymentMethod.getId(), paymentMethod);
    }

    @Override
    public PaymentMethod activatePaymentMethod(Long id) {
        PaymentMethod paymentMethod = getPaymentMethodById(id);
        if (paymentMethodRepository.findByNameAndIsActiveIsTrue(paymentMethod.getName()).isEmpty()) {
            throw new ConflictException(String.format("Payment method with name %s already active", paymentMethod.getName()));
        }
        paymentMethod.setIsActive(true);
        return updatePaymentMethod(paymentMethod.getId(), paymentMethod);
    }

    private String GenerateLogoFileName(PaymentMethod paymentMethod) {
        return String.format("%s-%s-%s-%s", paymentMethod.getName(), paymentMethod.getType(), paymentMethod.getCode(), paymentMethod.getProvider()).toLowerCase();
    }
}

