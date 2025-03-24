package com.thushan.user_Service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpKafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void otpRequestSend(String email) {
        try {
            kafkaTemplate.send("otp_request", email)
                    .thenAccept(result -> log.info(" Successfully sent OTP request for email: {}", email))
                    .exceptionally(ex -> {
                        log.error(" Failed to send OTP request for email: {}", email, ex);
                        return null;
                    });
        } catch (Exception ex) {
            log.error(" Unexpected failure sending OTP request for email: {}", email, ex);
        }
    }
}
