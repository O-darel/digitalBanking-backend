package com.bank.transaction_service.Transaction.Dtos;


import com.bank.transaction_service.Transaction.Entity.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEditDto {
    private TransactionStatus status;
}

