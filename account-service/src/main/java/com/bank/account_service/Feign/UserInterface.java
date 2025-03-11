package com.bank.account_service.Feign;

import com.bank.account_service.Account.DTO.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("USER-SERVICE")
public interface UserInterface {
    @GetMapping("/api/users/get-user")
    public UserResponseDto getUserByUuid();
}
