package com.bank.user_service.User.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {

    private String name;
    private String email;
    private String password;
}


