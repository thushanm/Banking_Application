package com.thushan.otp_Service.otp_Service.kafka;

import com.thushan.otp_Service.otp_Service.dto.OTPRequestDTO;
import com.thushan.otp_Service.otp_Service.service.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpKafkaConsumer {

    private final OTPService otpService;

    @KafkaListener(topics = "otp_request",groupId ="otp_sender")
    public void processOTPRequest(String email){
        log.info("OTP Request Received: {}",email);
        OTPRequestDTO requestDTO = new OTPRequestDTO(email);
        otpService.genarateOTP(requestDTO);
    }
}
