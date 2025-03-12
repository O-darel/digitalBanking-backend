package com.bank.account_service.Account.Service;

import com.bank.account_service.Account.DTO.*;
import com.bank.account_service.Account.Entity.Account;
import com.bank.account_service.Account.Repository.AccountRepository;
import com.bank.account_service.Event.AccountEvent;
import com.bank.account_service.Feign.UserInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserInterface userInterface;

    @Autowired
    private KafkaTemplate<String, AccountEvent> kafkaTemplate;

    private static final String ACCOUNT_NUMBER_PREFIX = "127";
    private static final int MAX_DIGITS = 3;
    @Transactional
    public String generateUniqueAccountNumber() {
        Account latestAccount = accountRepository.findFirstByOrderByAccountNumberDesc();

        if (latestAccount == null) {
            return ACCOUNT_NUMBER_PREFIX + String.format("%03d", 1);
        }

        String latestAccountNumber = latestAccount.getAccountNumber();
        int lastNumber = Integer.parseInt(latestAccountNumber.substring(3));
        int newNumber = lastNumber + 1;

        return ACCOUNT_NUMBER_PREFIX + String.format("%03d", newNumber);
    }


    public AccountResponse createAccount(CreateAccount input){
        try {
            UserResponseDto userResponseDto = userInterface.getUserByUuid();
            Account account = new Account();
            account.setAccountNumber(generateUniqueAccountNumber());
            account.setAccountStatus(input.getAccountStatus());
            account.setAccountType(input.getAccountType());
            account.setCustomerId(userResponseDto.getUuid());
            account.setAccountBalance(0.00);
            kafkaTemplate.send("account-topic", new AccountEvent(account.getAccountNumber(), userResponseDto.getEmail()));
            accountRepository.save(account);
            return AccountResponse.builder()
                    .accountType(account.getAccountType())
                    .accountNumber(account.getAccountNumber())
                    .accountStatus(account.getAccountStatus())
                    .accountOpeningDate(account.getAccountOpeningDate())
                    .accountBalance(account.getAccountBalance())
                    .build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public List<AccountResponse> getAllAccounts(){
        List<Account> accounts =  accountRepository.findAll();
        return accounts.stream()
                .map(account->AccountResponse.builder()
                        .accountStatus(account.getAccountStatus())
                        .accountNumber(account.getAccountNumber())
                        .accountOpeningDate(account.getAccountOpeningDate())
                        .accountType(account.getAccountType())
                        .accountBalance(account.getAccountBalance())
                        .build())
                .collect(Collectors.toList());
    }

    public AccountResponse getAccountById(Long accountId){
        try {
            Account account = accountRepository.findById(accountId).orElseThrow();
            return AccountResponse.builder()
                    .accountType(account.getAccountType())
                    .accountStatus(account.getAccountStatus())
                    .accountBalance(account.getAccountBalance())
                    .accountOpeningDate(account.getAccountOpeningDate())
                    .accountNumber(account.getAccountNumber())
                    .build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public Account updateAccount(UpdateAccount input, Long accountId){
        try{
            Account account = accountRepository.findById(accountId).orElseThrow(()-> new RuntimeException("account does not exist"));
            account.setAccountStatus(input.getAccountStatus());
            account.setAccountType(input.getAccountType());
            accountRepository.save(account);
            return account;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public String deleteAccount(Long accountId){
        try {
            Account account = accountRepository.findById(accountId).orElseThrow();
            accountRepository.delete(account);
            return "account deleted successfully";
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //update account balance
//    public Account updateAccountBalance(AccountUpdateDto accountUpdateDto){
//        try{
//            //get account
//            Account account= accountRepository.findById(accountUpdateDto.getAccountId()).orElseThrow();
//            Double newBalance =account.getAccountBalance() + account.getAccountBalance();
//
//            //update balance
//            account.setAccountBalance(newBalance);
//            return  accountRepository.save(account);
//        } catch (RuntimeException e){
//            throw  new RuntimeException(e.getMessage());
//        }
//
//    }


    public Account updateAccountBalance(AccountUpdateDto accountUpdateDto) {
        // Get account
        Account account = accountRepository.findById(accountUpdateDto.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        // Calculate new balance correctly
        Double newBalance = account.getAccountBalance() + accountUpdateDto.getAmount();
        // Update balance
        account.setAccountBalance(newBalance);

        //log this transaction to mongo
        return accountRepository.save(account);
    }


}
