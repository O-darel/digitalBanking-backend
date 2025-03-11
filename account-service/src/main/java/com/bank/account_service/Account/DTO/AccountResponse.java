package com.bank.account_service.Account.DTO;

import com.bank.account_service.Account.Entity.Account;
import com.bank.account_service.Account.Entity.AccountStatus;
import com.bank.account_service.Account.Entity.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {

    private String accountNumber;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private Date accountOpeningDate;
    private Double accountBalance;

    public AccountResponse(Account account) {
        this.accountType = account.getAccountType();
        this.accountNumber= getAccountNumber();
        this.accountStatus = account.getAccountStatus();
        this.accountOpeningDate = account.getAccountOpeningDate();
        this.accountBalance = account.getAccountBalance();
    }
}