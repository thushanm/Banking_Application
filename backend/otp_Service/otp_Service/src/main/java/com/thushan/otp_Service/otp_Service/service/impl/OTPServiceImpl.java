package com.thushan.otp_Service.otp_Service.service.impl;

import com.thushan.otp_Service.otp_Service.dto.OTPRequestDTO;
import com.thushan.otp_Service.otp_Service.dto.OTPResponseDTO;
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
    private final ValueOperations<String,String> valueOperations;
    private final JavaMailSender mailSender;
    @Override
    public OTPResponseDTO genarateOTP(OTPRequestDTO otpRequestDTO) {
        String otp = genarateSecureOTP();
        String emailKey = "OTP_" + otpRequestDTO.getEmail();

        // Save OTP in Redis
        valueOperations.set(emailKey, otp, 5, TimeUnit.MINUTES);
        log.info("OTP generated for {}: {}", otpRequestDTO.getEmail(), otp);

        // Send OTP Email
        sendOTPEmail(otpRequestDTO.getEmail(), otp);

        return new OTPResponseDTO("OTP sent Successfully");
    }


    @Override
    public OTPResponseDTO validateOTP(String email, String otp) {
        String emailKey = "OTP_"+ email;
        String storedOtp = valueOperations.get(emailKey);

        if(storedOtp == null){
            return new OTPResponseDTO("OTP Not Found");
        }
        if (!storedOtp.equals(otp)) {
            return new OTPResponseDTO("OTP Incorrect");
        }
        valueOperations.getOperations().delete(emailKey);
        return new OTPResponseDTO("OTP Validate Successfully");
    }
    private String genarateSecureOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(9000000);
        return String.valueOf(otp);
    }
    private void sendOTPEmail(String email,String otp){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Your OTP Code");
            mailMessage.setText("Your OTP Code Is "+otp+".It Will Expire in 30 second");
            mailSender.send(mailMessage);
            log.info("OTP email sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send OTP email: {}", e.getMessage());
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }
}
