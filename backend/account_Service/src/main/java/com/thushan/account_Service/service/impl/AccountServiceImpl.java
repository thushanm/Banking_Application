package com.thushan.account_Service.service.impl;

import com.thushan.account_Service.dto.AccountDTO;
import com.thushan.account_Service.entity.Account;
import com.thushan.account_Service.exception.CustomException;
import com.thushan.account_Service.repository.AccountRepository;
import com.thushan.account_Service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AccountDTO createAccount(Long userId) throws CustomException {
        if (accountRepository.findByUserId(userId).isPresent()) {
            throw new CustomException("User already has an account.");
        }

        Account account = new Account();
        account.setUserId(userId);
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(generateUniqueAccountNumber());

        Account savedAccount = accountRepository.save(account);
        return modelMapper.map(savedAccount, AccountDTO.class);
    }

    @Override
    public AccountDTO getAccountByUserId(Long userId) throws CustomException {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException("Account not found for user ID: " + userId));
        return modelMapper.map(account, AccountDTO.class);
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%010d", new Random().nextLong(1_000_000_000L));
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }
}

