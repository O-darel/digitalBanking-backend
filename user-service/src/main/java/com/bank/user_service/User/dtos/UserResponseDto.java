package com.bank.user_service.User.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    private boolean exists;
    private String name;
    private String email;
}
