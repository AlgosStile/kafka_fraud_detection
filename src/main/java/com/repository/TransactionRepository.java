package com.repository;


import com.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Modifying
    @Query("UPDATE Transaction t SET t.status = com.model.Transaction.Status.SUSPICIOUS WHERE t.userId = :userId")
    void markSuspicious(@Param("userId") Long userId);
}