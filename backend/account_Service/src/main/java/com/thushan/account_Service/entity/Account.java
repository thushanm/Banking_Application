package com.thushan.account_Service.entity;


import com.thushan.account_Service.Enumaration.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;
    @Column(nullable = false)
    private String holderName;

    @Column(nullable = false, unique = true)
    private String nicNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Column(nullable = false)
    private BigDecimal balance;
    @Column(nullable = false)
    private BigDecimal interest;

    @Column(nullable = false, unique = true)
    private Long userId;
}
