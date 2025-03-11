package com.bank.account_service.Customers.Repository;

import com.bank.account_service.Customers.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
