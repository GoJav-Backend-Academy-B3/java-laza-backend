package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionValidator {
    public void validateTransactionNotFound(Optional<Transaction> transaction, String id) {
        if (transaction.isEmpty()) {
            throw new NotFoundException(String.format("transaction with id %s not found", id));
        }
    }
}
