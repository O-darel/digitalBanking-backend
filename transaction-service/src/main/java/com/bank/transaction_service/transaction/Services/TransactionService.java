package com.bank.transaction_service.transaction.Services;


<<<<<<< Updated upstream:transaction-service/src/main/java/com/bank/transaction_service/transaction/Services/TransactionService.java
import com.bank.transaction_service.transaction.Dtos.TransactionDto;
import com.bank.transaction_service.transaction.Dtos.TransactionEditDto;
import com.bank.transaction_service.transaction.Dtos.TransactionInputDto;
import com.bank.transaction_service.transaction.Dtos.UpdateAccountBalanceDto;
import com.bank.transaction_service.transaction.Entity.Transaction;
import com.bank.transaction_service.transaction.Entity.TransactionStatus;
import com.bank.transaction_service.transaction.FeignClient.AccountClient;
import com.bank.transaction_service.transaction.FeignClient.Dtos.UserAccountDto;
import com.bank.transaction_service.transaction.Repositories.TransactionRepository;
=======
import com.bank.transaction_service.Event.TransactionEvent;
import com.bank.transaction_service.Transaction.Dtos.TransactionDto;
import com.bank.transaction_service.Transaction.Dtos.TransactionEditDto;
import com.bank.transaction_service.Transaction.Dtos.TransactionInputDto;
import com.bank.transaction_service.Transaction.Dtos.UpdateAccountBalanceDto;
import com.bank.transaction_service.Transaction.Entity.Transaction;
import com.bank.transaction_service.Transaction.Entity.TransactionStatus;
import com.bank.transaction_service.Transaction.Entity.TransactionType;
import com.bank.transaction_service.Transaction.FeignClient.AccountClient;
import com.bank.transaction_service.Transaction.FeignClient.Dtos.UserAccountDto;
import com.bank.transaction_service.Transaction.Repositories.TransactionRepository;
>>>>>>> Stashed changes:transaction-service/src/main/java/com/bank/transaction_service/Transaction/Services/TransactionService.java
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private  final AccountClient accountClient;
    @Autowired
    private KafkaTemplate<String, TransactionEvent> kafkaTemplate;


    @Transactional
    public TransactionDto recordTransaction(TransactionInputDto transactionInoutDto) {
//        Account account = accountRepository.findById(transactionInoutDto.getAccountId())
//                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        UserAccountDto userAccountDto=accountClient.getAccountById(transactionInoutDto.getAccountId());

        if(userAccountDto.getExists()){

            Transaction transaction = new Transaction();
            transaction.setAccount(transactionInoutDto.getAccountId());
            transaction.setTransactionType(transactionInoutDto.getTransactionType());
            transaction.setAmount(transactionInoutDto.getAmount());
            transaction.setStatus(transactionInoutDto.getStatus());
            transaction.setTransactionDate(LocalDateTime.now());

            // If transaction is approved, update account balance
            if (transactionInoutDto.getStatus() == TransactionStatus.APPROVED) {

                UpdateAccountBalanceDto updateAccountBalanceDto=new UpdateAccountBalanceDto();
                updateAccountBalanceDto.setAmount(transactionInoutDto.getAmount());
                updateAccountBalanceDto.setAccountId(transactionInoutDto.getAccountId());
                updateAccountBalanceDto.setTransactionType(transactionInoutDto.getTransactionType());

                accountClient.updateAccountBalance(updateAccountBalanceDto);
            }

            Transaction savedTransaction = transactionRepository.save(transaction);
            return mapToDTO(savedTransaction);

        }
        // Return null or throw an exception if account does not exist
        throw new EntityNotFoundException("Account does not exist.");
    }

    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TransactionDto getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        return mapToDTO(transaction);
    }

    @Transactional
    public TransactionDto updateTransaction(Long transactionId, TransactionEditDto transactionDTO) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        boolean wasApproved = transaction.getStatus() == TransactionStatus.APPROVED;
        boolean isNowApproved = transactionDTO.getStatus() == TransactionStatus.APPROVED;

        //transaction.setTransactionType(transactionDTO.getTransactionType());
        //transaction.setAmount(transactionDTO.getAmount());
        transaction.setStatus(transactionDTO.getStatus());

        if (!wasApproved && isNowApproved) {
            UpdateAccountBalanceDto updateAccountBalanceDto=new UpdateAccountBalanceDto();
            updateAccountBalanceDto.setTransactionType(transaction.getTransactionType());
            updateAccountBalanceDto.setAmount(transaction.getAmount());
            updateAccountBalanceDto.setAccountId(transaction.getAccount());

            accountClient.updateAccountBalance(updateAccountBalanceDto);

        }
        //kafkaTemplate.send("transaction-topic", new TransactionEvent());
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToDTO(updatedTransaction);
    }


//    @Transactional
//    public void deleteTransaction(Long transactionId) {
//        Transaction transaction = transactionRepository.findById(transactionId)
//                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
//
//        if (transaction.getStatus() == TransactionStatus.APPROVED) {
//            reverseAccountBalance(transaction);
//        }
//
//        transactionRepository.delete(transaction);
//    }

//    private void reverseAccountBalance(Transaction transaction) {
//        Account account = transaction.getAccount();
//        BigDecimal amount = transaction.getAmount();
//
//        if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
//
//            account.setBalance(account.getBalance().subtract(amount));
//            // Reverse deposit
////            UpdateAccountBalanceDto updateAccountBalanceDto=new UpdateAccountBalanceDto();
////
////            updateAccountBalanceDto.setTransactionType(transaction.getTransactionType());
////            updateAccountBalanceDto.setAmount(transaction.getAmount());
////            updateAccountBalanceDto.setAccountId(transaction.getAccount());
//
//            accountClient.updateAccountBalance(updateAccountBalanceDto);
//
//        } else if (transaction.getTransactionType() == TransactionType.WITHDRAWAL) {
//            account.setBalance(account.getBalance().add(amount)); // Reverse withdrawal
//        }
//        accountRepository.save(account);
//    }


    private TransactionDto mapToDTO(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccount(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getTransactionDate()
        );
    }
}


