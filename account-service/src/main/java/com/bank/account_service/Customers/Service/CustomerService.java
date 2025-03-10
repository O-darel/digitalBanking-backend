package com.bank.account_service.Customers.Service;

import com.bank.account_service.Customers.DTO.CreateCustomer;
import com.bank.account_service.Customers.DTO.CustomerResponse;
import com.bank.account_service.Customers.DTO.UpdateCustomer;
import com.bank.account_service.Customers.Entity.Customer;
import com.bank.account_service.Customers.Repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    private static final String ACCOUNT_NUMBER_PREFIX = "127";
    private static final int MAX_DIGITS = 3;


    private String convertImageToBase64(MultipartFile file) throws IOException {
        byte[] fileContent = file.getBytes();
        return Base64.getEncoder().encodeToString(fileContent);
    }


    public Customer createCustomer(CreateCustomer input, MultipartFile profilePicture) throws IOException {
        Customer customer = new Customer();
        customer.setFirstName(input.getFirstName());
        customer.setLastName(input.getLastName());
        customer.setDateOfBirth(input.getDateOfBirth());
        customer.setEmail(input.getEmail());
        customer.setGender(input.getGender());
        customer.setPhoneNumber(input.getPhoneNumber());
        customer.setAddress(input.getAddress());

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String base64ProfilePicture = convertImageToBase64(profilePicture);
            customer.setCustomerProfilePictureBase64(base64ProfilePicture);
        }

        customerRepository.save(customer);
        return customer;
    }
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> CustomerResponse.builder()
                        .customerId(customer.getCustomerId())
                        .firstName(customer.getFirstName())
                        .phoneNumber(customer.getPhoneNumber())
                        .lastName(customer.getLastName())
                        .email(customer.getEmail())
                        .dateOfBirth(customer.getDateOfBirth())
                        .gender(customer.getGender())
                        .build())
                .collect(Collectors.toList());
    }
    public Customer updateCustomerDetails(UpdateCustomer input, Long customerId){
        try {
            Customer customer = customerRepository.findById(customerId).orElseThrow();
            customer.setEmail(input.getEmail());
            customer.setLastName(input.getLastName());
            customer.setDateOfBirth(input.getDateOfBirth());
            customer.setGender(input.getGender());
            customer.setFirstName(input.getFirstName());
            customer.setPhoneNumber(input.getPhoneNumber());
            customerRepository.save(customer);
            return customer;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    public Optional<Customer> getCustomerDetails(Long customerId){
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer;
    }
    public String deleteCustomer(Long customerId){
        try {
            Customer customer = customerRepository.findById(customerId).orElseThrow(()->new RuntimeException("cannot find user"));
            customerRepository.delete(customer);
            return "customer deleted successfully!!";
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
