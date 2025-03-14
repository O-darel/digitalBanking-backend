package com.bank.transaction_service.transaction.FeignClient.Dtos;


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

