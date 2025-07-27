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

    public void sendOtpRequest(String email) {
        kafkaTemplate.send("otp_request", email);
        log.info("OTP request sent to Kafka for {}", email);
    }

    public void sendOtpValidationResult(String validationResult) {
        kafkaTemplate.send("otp_response", validationResult);
        log.info("OTP validation result sent to Kafka: {}", validationResult);
    }
    public void sendUserId(Long userId) {

        kafkaTemplate.send("user_create_topic", String.valueOf(userId));
        log.info("User id sent to Kafka for {}", userId);
    }
}
