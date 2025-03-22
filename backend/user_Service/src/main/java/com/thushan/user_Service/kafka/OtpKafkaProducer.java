package com.thushan.user_Service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j

public class OtpKafkaProducer {
    private final KafkaTemplate<String,String> kafkaTemplate;
     public void otpRequestSend(String email){
         log.info("OTP request send email: {}",email);
         kafkaTemplate.send("otp_request",email);
     }
}
