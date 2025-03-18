package com.bank.transaction_service.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEvent {
    private Double amount;
    private String transactionType;
    private String email;
}
