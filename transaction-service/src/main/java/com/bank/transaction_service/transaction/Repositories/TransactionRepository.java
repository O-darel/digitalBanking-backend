package com.bank.transaction_service.transaction.Repositories;

import com.bank.transaction_service.transaction.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
