package com.bank.transaction_service.transaction.Dtos;


import com.bank.transaction_service.transaction.Entity.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEditDto {
    private TransactionStatus status;
}

