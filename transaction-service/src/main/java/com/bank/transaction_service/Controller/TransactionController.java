package com.bank.transaction_service.Controller;

import com.bank.transaction_service.Transaction.Dtos.TransactionDto;
import com.bank.transaction_service.Transaction.Dtos.TransactionEditDto;
import com.bank.transaction_service.Transaction.Dtos.TransactionInputDto;
import com.bank.transaction_service.Transaction.Services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "APIs for managing transactions and account balances")
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "Record a new transaction",
            description = "Records a new transaction and updates the account balance if the transaction is approved.")
    @ApiResponse(responseCode = "201", description = "Transaction recorded successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PostMapping
    public ResponseEntity<TransactionDto> recordTransaction(@Valid @RequestBody TransactionInputDto transactionDTO) {
        TransactionDto recordedTransaction = transactionService.recordTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(recordedTransaction);
    }

    @Operation(summary = "Get all transactions", description = "Retrieves a list of all transactions.")
    @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully")
    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @Operation(summary = "Get transaction by ID", description = "Retrieves transaction details using the transaction ID.")
    @ApiResponse(responseCode = "200", description = "Transaction details retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }

    @Operation(summary = "Update a transaction",
            description = "Updates transaction details. If status changes to Approved, the account balance is updated.")
    @ApiResponse(responseCode = "200", description = "Transaction updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable Long transactionId,
                                                            @Valid @RequestBody TransactionEditDto transactionDTO) {
        return ResponseEntity.ok(transactionService.updateTransaction(transactionId, transactionDTO));
    }


//    @Operation(summary = "Delete a transaction",
//            description = "Deletes a transaction. If it was approved, the balance update is reversed.")
//    @ApiResponse(responseCode = "204", description = "Transaction deleted successfully")
//    @ApiResponse(responseCode = "404", description = "Transaction not found")
//    @DeleteMapping("/{transactionId}")
//    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
//        transactionService.deleteTransaction(transactionId);
//        return ResponseEntity.noContent().build();
//    }
}