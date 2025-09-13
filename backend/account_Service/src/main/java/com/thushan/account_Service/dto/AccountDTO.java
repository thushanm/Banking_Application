package com.thushan.account_Service.dto;

import com.thushan.account_Service.Enumaration.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String holderName;
    private String nicNo;
    private AccountType accountType;
    private BigDecimal balance;
    private BigDecimal interest;
    private Long userId;

}

