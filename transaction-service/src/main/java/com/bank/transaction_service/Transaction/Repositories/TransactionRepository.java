package com.bank.transaction_service.Transaction.Repositories;

import com.bank.transaction_service.Transaction.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
