package com.bank.account_service.Account.Controller;

import com.bank.account_service.Account.DTO.AccountResponse;
import com.bank.account_service.Account.DTO.CreateAccount;
import com.bank.account_service.Account.DTO.UpdateAccount;
import com.bank.account_service.Account.Entity.Account;
import com.bank.account_service.Account.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PostMapping("/new")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccount input) {
        try {
            AccountResponse account = accountService.createAccount(input);
            return new ResponseEntity<>(account, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        try {
            List<AccountResponse> accounts = accountService.getAllAccounts();
            return new ResponseEntity<>(accounts, HttpStatus.OK);  // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long accountId) {
        try {
            AccountResponse account = accountService.getAccountById(accountId);

            return new ResponseEntity<>(account, HttpStatus.OK);  // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // 404 Not Found
        }
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity<Account> updateAccount(@RequestBody UpdateAccount input, @PathVariable Long accountId) {
        try {
            Account updatedAccount = accountService.updateAccount(input, accountId);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);  // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 Bad Request
        }
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        try {
            String message = accountService.deleteAccount(accountId);
            return new ResponseEntity<>(message, HttpStatus.OK);  // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<>
}
