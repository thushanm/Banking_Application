package com.thushan.otp_Service.otp_Service.service.impl;

import com.thushan.otp_Service.otp_Service.dto.OTPRequestDTO;
import com.thushan.otp_Service.otp_Service.service.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OTPServiceImpl implements OTPService {
    private final ValueOperations<String, String> valueOperations;
    private final JavaMailSender mailSender;

    @Override
    public OTPRequestDTO genarateOTP(OTPRequestDTO otpRequestDTO) {
        String otp = generateSecureOTP();

        String emailKey = "otp:" + otpRequestDTO.getEmail();

        valueOperations.set(emailKey, otp, 10, TimeUnit.MINUTES);
        log.info("OTP generated for {}: {}", otpRequestDTO.getEmail(), otp);

        sendOTPEmail(otpRequestDTO.getEmail(), otp);


        return new OTPRequestDTO(otpRequestDTO.getEmail(), otp);
    }

    private String generateSecureOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void sendOTPEmail(String email, String otp) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Your OTP Code");
            mailMessage.setText("Your OTP Code is " + otp + ". It will expire in 10 minutes.");
            mailSender.send(mailMessage);
            log.info("OTP email sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send OTP email: {}", e.getMessage());
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }
}
