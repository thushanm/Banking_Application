package com.thushan.account_Service.kafka;

import com.thushan.account_Service.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCreationConsumer {
    private final AccountService accountService;

    @KafkaListener(topics = "user_create_topic",groupId = "account-service-group")
    public void handleUserCreation(String userIDString) {
        log.info("Received user creation message: " + userIDString);
        try {
            Long userId = Long.parseLong(userIDString);
            accountService.createAccount(userId);
        } catch (NumberFormatException e) {
            log.error("Invalid user ID received from Kafka: {}", userIDString, e);
        } catch (Exception e) {
            log.error("Failed to create account for user ID {}: {}", userIDString, e.getMessage());
        }

    }
}
