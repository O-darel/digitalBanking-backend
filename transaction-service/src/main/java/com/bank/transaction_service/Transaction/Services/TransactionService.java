package com.bank.transaction_service.Transaction.Services;


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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private  final AccountClient accountClient;


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
                updateAccountBalanceDto.setTransactionType(transactionInoutDto.getTransactionType());
                updateAccountBalance(account, updateAccountBalanceDto);
            }

            Transaction savedTransaction = transactionRepository.save(transaction);
            return mapToDTO(savedTransaction);

        }

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
            updateAccountBalance(transaction.getAccount(), updateAccountBalanceDto);
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToDTO(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        if (transaction.getStatus() == TransactionStatus.APPROVED) {
            reverseAccountBalance(transaction);
        }

        transactionRepository.delete(transaction);
    }

    private void updateAccountBalance(Account account,UpdateAccountBalanceDto updateAccountBalanceDto) {
        BigDecimal amount = updateAccountBalanceDto.getAmount();

        if (updateAccountBalanceDto.getTransactionType() == TransactionType.DEPOSIT) {
            account.setBalance(account.getBalance().add(amount)); // Use add() for deposit
        } else if (updateAccountBalanceDto.getTransactionType() == TransactionType.WITHDRAWAL) {
            if (account.getBalance().compareTo(amount) < 0) { // Compare instead of <
                throw new IllegalArgumentException("Insufficient balance");
            }
            account.setBalance(account.getBalance().subtract(amount)); // Use subtract() for withdrawal
        }
        accountRepository.save(account);
    }

    private void reverseAccountBalance(Transaction transaction) {
        Account account = transaction.getAccount();
        BigDecimal amount = transaction.getAmount();

        if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
            account.setBalance(account.getBalance().subtract(amount)); // Reverse deposit
        } else if (transaction.getTransactionType() == TransactionType.WITHDRAWAL) {
            account.setBalance(account.getBalance().add(amount)); // Reverse withdrawal
        }
        accountRepository.save(account);
    }


    private TransactionDto mapToDTO(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getTransactionDate()
        );
    }
}


