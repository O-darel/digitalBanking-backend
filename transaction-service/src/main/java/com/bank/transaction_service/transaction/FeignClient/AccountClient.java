package com.bank.transaction_service.transaction.FeignClient;

import com.bank.transaction_service.transaction.Dtos.UpdateAccountBalanceDto;
import com.bank.transaction_service.transaction.FeignClient.Dtos.UserAccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping("api/accounts/{id}")
    UserAccountDto getAccountById(Long Id);

    @PutMapping("api/accounts/update-balance")
    String updateAccountBalance(@RequestBody UpdateAccountBalanceDto updateAccountBalanceDto);
}
