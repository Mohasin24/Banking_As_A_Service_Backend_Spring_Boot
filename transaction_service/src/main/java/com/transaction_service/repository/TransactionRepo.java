package com.transaction_service.repository;

import com.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Long> {
    List<Transaction> findByFromAccountId(long fromAccountId);
    List<Transaction> findByUserId(long userId);
}
