package com.thushan.account_Service.service.impl;
import com.thushan.account_Service.dto.AccountDTO;
import com.thushan.account_Service.entity.Account;
import com.thushan.account_Service.Enumaration.AccountType;
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
    public AccountDTO createAccount(Long userId, AccountDTO accountDetails) throws CustomException {

        if (accountRepository.findByUserId(userId).isPresent()) {
            throw new CustomException("User already has an account.");
        }

        if (accountDetails.getHolderName() == null || accountDetails.getNicNo() == null || accountDetails.getAccountType() == null) {
            throw new CustomException("Holder name, NIC, and account type are required.");
        }

        Account account = new Account();
        account.setUserId(userId);
        account.setHolderName(accountDetails.getHolderName());
        account.setNicNo(accountDetails.getNicNo());


        account.setAccountType(accountDetails.getAccountType());


        account.setBalance(BigDecimal.ZERO);
        account.setInterest(BigDecimal.valueOf(0.02));
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

    @Override
    @Transactional
    public void updateBalance(String accountNumber, BigDecimal amount) throws CustomException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new CustomException("Account not found with account number: " + accountNumber));

        BigDecimal newBalance = account.getBalance().add(amount);
        // Ensure the balance does not drop below zero.
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException("Insufficient funds.");
        }

        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    private String generateUniqueAccountNumber() {
        Random random = new Random();
        String accountNumber;
        do {

            long number = random.nextLong(1_000_000_000L);
            accountNumber = String.format("%010d", number);
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }
}
