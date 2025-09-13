package com.thushan.account_Service.service;


import com.thushan.account_Service.dto.AccountDTO;
import com.thushan.account_Service.exception.CustomException;

import java.math.BigDecimal;


public interface AccountService {

    AccountDTO createAccount(Long userId, AccountDTO accountDetails) throws CustomException;
    AccountDTO getAccountByUserId(Long userId) throws CustomException;
    void updateBalance(String accountNumber, BigDecimal amount) throws CustomException;
}
