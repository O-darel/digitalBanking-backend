package com.bank.account_service.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("USER-SERVICE")
public interface UserInterface {
    @GetMapping("/api/users/{uuid}")
    public ResponseEntity<UserResponseDto> getUserByUuid(@PathVariable String uuid);
}
