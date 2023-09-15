package com.phincon.laza.service;

import java.util.List;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.model.dto.request.CreateUpdateCreditCardRequest;
import com.phincon.laza.model.entity.CreditCard;

public interface CreditCardService {

    public List<CreditCard> getAll(String userId);

    public CreditCard getById(String id);

    public CreditCard create(String userId, CreateUpdateCreditCardRequest request) throws ConflictException;
}
