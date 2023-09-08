package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.model.entity.Transaction;
import com.phincon.laza.repository.TransactionRepository;
import com.phincon.laza.service.TransactionService;
import com.phincon.laza.validator.TransactionValidator;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionValidator transactionValidator) {
        this.transactionRepository = transactionRepository;
        this.transactionValidator = transactionValidator;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(String id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);

        transactionValidator.validateTransactionNotFound(transaction, id);

        return transaction.get();
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        try {
            return transactionRepository.save(transaction);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }
}
