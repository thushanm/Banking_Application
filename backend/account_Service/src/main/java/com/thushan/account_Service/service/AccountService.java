package com.thushan.account_Service.service;


import com.thushan.account_Service.dto.AccountDTO;
import com.thushan.account_Service.exception.CustomException;

public interface AccountService {
    AccountDTO createAccount(Long userId) throws CustomException;
    AccountDTO getAccountByUserId(Long userId) throws CustomException;
}