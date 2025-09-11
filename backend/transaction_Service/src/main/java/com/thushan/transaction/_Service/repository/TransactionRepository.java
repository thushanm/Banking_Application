package com.thushan.transaction._Service.repository;

import com.thushan.transaction._Service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountOrToAccount(String fromAccount, String toAccount);
}
