package com.thushan.transaction._Service.service;


import com.thushan.transaction._Service.dto.TransactionDTO;
import com.thushan.transaction._Service.dto.TransferRequestDTO;

import java.util.List;

public interface TransactionService {
    TransactionDTO createTransaction(TransferRequestDTO transferRequestDTO);
    List<TransactionDTO> getTransactionsByAccountNumber(String accountNumber);
}
