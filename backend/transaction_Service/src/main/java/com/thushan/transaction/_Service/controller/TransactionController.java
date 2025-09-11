package com.thushan.transaction._Service.controller;


import com.thushan.transaction._Service.dto.TransactionDTO;
import com.thushan.transaction._Service.dto.TransferRequestDTO;
import com.thushan.transaction._Service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransferRequestDTO transferRequestDTO) {
        return ResponseEntity.ok(transactionService.createTransaction(transferRequestDTO));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountNumber(accountNumber));
    }
}
