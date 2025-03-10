package com.bank.account_service.Account.Repository;

import com.bank.account_service.Account.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findFirstByOrderByAccountNumberDesc();
}
