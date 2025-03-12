package com.bank.transaction_service.Transaction.Dtos;

import com.bank.transaction_service.Transaction.Entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountBalanceDto {
    private Long accountId;
    private TransactionType transactionType;
    private Double amount;
}

