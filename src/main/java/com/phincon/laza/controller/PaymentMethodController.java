package com.phincon.laza.controller;

import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.PaymentMethod;
import com.phincon.laza.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "X-AUTH-TOKEN")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<DataResponse<List<PaymentMethod>>> getAllPaymentMethods() {
        return DataResponse.ok(paymentMethodService.getAllPaymentMethods());
    }

    @GetMapping("/payment-methods/active")
    public ResponseEntity<DataResponse<List<PaymentMethod>>> getAllActivePaymentMethods() {
        return DataResponse.ok(paymentMethodService.getAllActivePaymentMethods());
    }

    @GetMapping("/payment-methods/{id}")
    public ResponseEntity<DataResponse<PaymentMethod>> getPaymentMethodById(@PathVariable Long id) {
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(id);
        return DataResponse.ok(paymentMethod);
    }

    @PostMapping("/management/payment-methods")
    public ResponseEntity<DataResponse<PaymentMethod>> createPaymentMethod(@Valid @RequestBody PaymentMethod paymentMethod) {
        PaymentMethod createdPaymentMethod = paymentMethodService.createPaymentMethod(paymentMethod);
        return DataResponse.created(createdPaymentMethod);
    }

    @PutMapping("/management/payment-methods/{id}")
    public ResponseEntity<DataResponse<PaymentMethod>> updatePaymentMethod(@PathVariable Long id, @Valid @RequestBody PaymentMethod updatedPaymentMethod) {
        PaymentMethod updated = paymentMethodService.updatePaymentMethod(id, updatedPaymentMethod);
        return DataResponse.ok(updated);
    }

    @PostMapping("/management/payment-methods/{id}/deactivate")
    public ResponseEntity<DataResponse<PaymentMethod>> deactivatedPaymentMethod(@PathVariable Long id) {
        PaymentMethod updatedPaymentMethod =  paymentMethodService.deactivatePaymentMethod(id);
        return DataResponse.ok(updatedPaymentMethod);
    }

    @PostMapping("/management/payment-methods/{id}/activate")
    public ResponseEntity<DataResponse<PaymentMethod>> activatePaymentMethod(@PathVariable Long id) {
        PaymentMethod updatedPaymentMethod =  paymentMethodService.activatePaymentMethod(id);
        return DataResponse.ok(updatedPaymentMethod);
    }
}
