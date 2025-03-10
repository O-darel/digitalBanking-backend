package com.bank.account_service.Customers.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
}
