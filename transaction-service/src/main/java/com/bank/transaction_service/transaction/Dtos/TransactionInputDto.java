package com.bank.transaction_service.transaction.Dtos;

import com.bank.transaction_service.transaction.Entity.TransactionStatus;
import com.bank.transaction_service.transaction.Entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInputDto {
    private Long accountId;
    private TransactionType transactionType;
    private Double amount;
    private TransactionStatus status;
}

