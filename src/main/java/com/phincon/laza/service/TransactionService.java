package com.phincon.laza.service;

import com.phincon.laza.model.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactions();

    Transaction getTransactionById(String id);

    Transaction updateTransaction(String id, Transaction transaction);

    Transaction getTransactionByReferenceId(String id);

    Transaction createTransaction(Transaction transaction);
}