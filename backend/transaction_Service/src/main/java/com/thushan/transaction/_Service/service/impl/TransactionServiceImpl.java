package com.thushan.transaction._Service.service.impl;




import com.thushan.transaction._Service.dto.TransactionDTO;
import com.thushan.transaction._Service.dto.TransferRequestDTO;
import com.thushan.transaction._Service.entity.Transaction;
import com.thushan.transaction._Service.entity.TransactionStatus;
import com.thushan.transaction._Service.entity.TransactionType;
import com.thushan.transaction._Service.kafka.TransactionKafkaProducer;
import com.thushan.transaction._Service.repository.TransactionRepository;
import com.thushan.transaction._Service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionKafkaProducer kafkaProducer;
    private final ModelMapper modelMapper;

    @Override
    public TransactionDTO createTransaction(TransferRequestDTO transferRequestDTO) {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(transferRequestDTO.getFromAccount());
        transaction.setToAccount(transferRequestDTO.getToAccount());
        transaction.setAmount(transferRequestDTO.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.PENDING);

        Transaction savedTransaction = transactionRepository.save(transaction);
        kafkaProducer.sendTransactionEvent("New transaction created: " + savedTransaction.getId());

        return modelMapper.map(savedTransaction, TransactionDTO.class);
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findByFromAccountOrToAccount(accountNumber, accountNumber);
        return transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }
}
