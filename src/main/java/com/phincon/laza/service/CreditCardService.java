package com.phincon.laza.service;

import java.util.List;

import com.phincon.laza.model.entity.CreditCard;

public interface CreditCardService {

    public List<CreditCard> getAll(String userId);
}
