package com.bank.transaction_service.transaction.Dtos;

import com.bank.transaction_service.transaction.Entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountBalanceDto {
    private Long accountId;
    private TransactionType transactionType;
    private Double amount;
}

