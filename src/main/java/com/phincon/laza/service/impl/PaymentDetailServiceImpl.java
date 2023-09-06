package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.model.entity.PaymentDetail;
import com.phincon.laza.repository.PaymentDetailRepository;
import com.phincon.laza.service.PaymentDetailService;
import com.phincon.laza.validator.PaymentDetailValidator;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentDetailServiceImpl implements PaymentDetailService {

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private PaymentDetailValidator paymentDetailValidator;

    @Override
    public List<PaymentDetail> getAllPaymentDetails() {
        return paymentDetailRepository.findAll();
    }

    @Override
    public PaymentDetail getPaymentDetailById(String id) {
        Optional<PaymentDetail> paymentDetail = paymentDetailRepository.findById(id);

        paymentDetailValidator.validatePaymentDetailNotFound(paymentDetail, id);

        return paymentDetail.get();
    }

    @Override
    public PaymentDetail createPaymentDetail(PaymentDetail paymentDetail) {
        try {
            return paymentDetailRepository.save(paymentDetail);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

}
