package com.thushan.transaction._Service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "account-service", url = "http://localhost:8083")
public interface AccountServiceClient {

    @PutMapping("/api/accounts/{accountNumber}/balance")
    void updateBalance(@PathVariable("accountNumber") String accountNumber, @RequestParam("amount") BigDecimal amount);
}
