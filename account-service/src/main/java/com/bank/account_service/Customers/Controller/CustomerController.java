package com.bank.account_service.Customers.Controller;

import com.bank.account_service.Customers.DTO.CreateCustomer;
import com.bank.account_service.Customers.DTO.CustomerResponse;
import com.bank.account_service.Customers.DTO.UpdateCustomer;
import com.bank.account_service.Customers.Entity.Customer;
import com.bank.account_service.Customers.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/new",consumes = "multipart/form-data")
    public ResponseEntity<Customer> createCustomer(@ModelAttribute CreateCustomer input, @RequestParam("profilePicture") MultipartFile profilePicture) {
        try {
            Customer createdCustomer = customerService.createCustomer(input, profilePicture);
            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        try {
            List<CustomerResponse> customers = customerService.getAllCustomers();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Optional<Customer>> getCustomerDetails(@PathVariable Long customerId) {
        try {
            Optional<Customer> customer = customerService.getCustomerDetails(customerId);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody UpdateCustomer input, @PathVariable Long customerId) {
        try {
            Customer updatedCustomer = customerService.updateCustomerDetails(input, customerId);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        try {
            String response = customerService.deleteCustomer(customerId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
