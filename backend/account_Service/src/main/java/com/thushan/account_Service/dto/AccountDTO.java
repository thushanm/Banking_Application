package com.thushan.account_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private BigDecimal interest;
    private Long userId;
}
