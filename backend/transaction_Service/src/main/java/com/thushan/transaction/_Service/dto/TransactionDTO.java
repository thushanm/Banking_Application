package com.thushan.transaction._Service.dto;



import com.thushan.transaction._Service.entity.TransactionStatus;
import com.thushan.transaction._Service.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private TransactionType type;
    private TransactionStatus status;
}
