package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.model.entity.PaymentMethod;
import com.phincon.laza.repository.PaymentMethodRepository;
import com.phincon.laza.service.PaymentMethodService;
import com.phincon.laza.validator.PaymentMethodValidator;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodValidator paymentMethodValidator;

    @Autowired
    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository, PaymentMethodValidator paymentMethodValidator) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodValidator = paymentMethodValidator;
    }

    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethod getPaymentMethodById(Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);

        paymentMethodValidator.validatePaymentMethodNotFound(paymentMethod, id);

        return paymentMethod.get();
    }

    public PaymentMethod createPaymentMethod(PaymentMethod paymentMethod) {
        try {
            return paymentMethodRepository.save(paymentMethod);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public PaymentMethod updatePaymentMethod(Long id, PaymentMethod updatedPaymentMethod) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        paymentMethodValidator.validatePaymentMethodNotFound(paymentMethod, id);

        updatedPaymentMethod.setId(id);
        return paymentMethodRepository.save(updatedPaymentMethod);
    }

    public void deletePaymentMethod(Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        paymentMethodValidator.validatePaymentMethodNotFound(paymentMethod, id);
        paymentMethodRepository.deleteById(id);
    }

    @Override
    public PaymentMethod deactivatePaymentMethod(Long id) {
        PaymentMethod paymentMethod = getPaymentMethodById(id);
        paymentMethod.setActive(false);
        return updatePaymentMethod(paymentMethod.getId(), paymentMethod);
    }

    @Override
    public PaymentMethod activatePaymentMethod(Long id) {
        PaymentMethod paymentMethod = getPaymentMethodById(id);
        paymentMethod.setActive(false);
        return updatePaymentMethod(paymentMethod.getId(), paymentMethod);
    }
}

