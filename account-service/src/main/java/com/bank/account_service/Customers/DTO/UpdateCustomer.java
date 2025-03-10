package com.bank.account_service.Customers.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomer {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
}
