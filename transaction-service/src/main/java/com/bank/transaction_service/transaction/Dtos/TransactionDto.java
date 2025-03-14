package com.bank.transaction_service.transaction.Dtos;



import com.bank.transaction_service.transaction.Entity.TransactionStatus;
import com.bank.transaction_service.transaction.Entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransactionDto {
    private Long id;
    private Long accountId;
    private TransactionType transactionType;
    private Double amount;
    private TransactionStatus status;
    private LocalDateTime transactionDate;
}


