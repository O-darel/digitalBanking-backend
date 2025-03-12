package com.bank.transaction_service.Transaction.FeignClient.Dtos;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDto {
    private Boolean exists;
}
