package com.phincon.laza.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.CreditCard;
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
}   

