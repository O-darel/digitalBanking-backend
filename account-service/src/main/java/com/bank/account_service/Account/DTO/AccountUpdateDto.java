package com.bank.account_service.Account.DTO;

import lombok.*;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateDto {

    private Long accountId;
    private Double amount;
    private String transactionType;

}
