package com.phincon.laza.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CreateUpdateCreditCardRequest;
import com.phincon.laza.model.entity.CreditCard;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.CreditCardRepository;
import com.phincon.laza.service.CreditCardService;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    private CreditCardRepository repository;

    @Override
    public List<CreditCard> getAll(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public CreditCard getById(String id) {
        return repository.findById(id).orElseThrow(() -> {
            return new NotFoundException("Credit Card not found");
        });
    }

    @Override
    public CreditCard create(String userId, CreateUpdateCreditCardRequest request) throws ConflictException {
        User u = new User();
        u.setId(userId);
        CreditCard e = new CreditCard(null, request.cardNumber(), request.expiryMonth(), request.expiryYear(), u);
        try {
            return repository.save(e);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Credit card already exists");
        }
    }
}
