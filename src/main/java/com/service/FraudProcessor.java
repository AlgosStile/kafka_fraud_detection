package com.service;

import com.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FraudProcessor {
    private final TransactionRepository repository;

    @Autowired
    public FraudProcessor(TransactionRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "fraud-alerts", groupId = "fraud-processor")
    public void handleFraudAlert(String message) {
        System.out.println("Received fraud alert: " + message);


        if (message != null && message.startsWith("Fraud detected")) {
            String[] parts = message.split(" ");
            Long userId = Long.parseLong(parts[2]);
            repository.markSuspicious(userId);
        }
    }
}