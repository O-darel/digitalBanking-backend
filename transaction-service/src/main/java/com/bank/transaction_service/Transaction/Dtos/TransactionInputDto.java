package com.bank.transaction_service.Transaction.Dtos;

import com.bank.transaction_service.Transaction.Entity.TransactionStatus;
import com.bank.transaction_service.Transaction.Entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInputDto {
    private Long accountId;
    private TransactionType transactionType;
    private Double amount;
    private TransactionStatus status;
}

