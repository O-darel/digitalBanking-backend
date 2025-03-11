package com.bank.account_service.Account.DTO;

import com.bank.account_service.Account.Entity.AccountStatus;
import com.bank.account_service.Account.Entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccount {
    private AccountType accountType;
    private AccountStatus accountStatus;
    private Long customerId;
}