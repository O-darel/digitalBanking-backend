package com.bank.transaction_service.Transaction.FeignClient;

import com.bank.transaction_service.Transaction.FeignClient.Dtos.UserAccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping("api/accounts/{id}")
    UserAccountDto getAccountById(Long Id);
}
