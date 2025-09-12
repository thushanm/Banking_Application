package com.thushan.account_Service.controller;


import com.thushan.account_Service.dto.AccountDTO;
import com.thushan.account_Service.exception.CustomException;
import com.thushan.account_Service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createAccount(@PathVariable Long userId) {
        try {
            AccountDTO newAccount = accountService.createAccount(userId);
            return ResponseEntity.status(201).body(newAccount);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAccountByUserId(@PathVariable Long userId) {
        try {
            AccountDTO account = accountService.getAccountByUserId(userId);
            return ResponseEntity.ok(account);
        } catch (CustomException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{accountNumber}/balance")
    public ResponseEntity<?> updateBalance(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        try {
            accountService.updateBalance(accountNumber, amount);
            return ResponseEntity.ok().build();
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
