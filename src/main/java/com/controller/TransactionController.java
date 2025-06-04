package com.controller;


import com.model.Transaction;
import com.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository repository;

    @Autowired
    public TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        transaction.setStatus(Transaction.Status.NEW);
        transaction.setTimestamp(LocalDateTime.now());
        return repository.save(transaction);
    }
}