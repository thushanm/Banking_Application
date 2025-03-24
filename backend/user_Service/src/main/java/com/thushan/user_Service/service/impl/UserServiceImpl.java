package com.thushan.user_Service.service.impl;

import com.thushan.user_Service.dto.UserDTO;
import com.thushan.user_Service.entity.User;
import com.thushan.user_Service.exceptions.CustomException;
import com.thushan.user_Service.kafka.OtpKafkaProducer;
import com.thushan.user_Service.repository.UserRepository;
import com.thushan.user_Service.security.JwtUtil;
import com.thushan.user_Service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
private final JwtUtil jwtUtil;
private KafkaTemplate<String, Object> kafkaTemplate;

    private final OtpKafkaProducer otpKafkaProducer;
    private final ConcurrentHashMap<String,CompletableFuture<Boolean>> futures = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public String register(UserDTO userDTO) throws CustomException {
        log.info("initiating registration of user {}", userDTO.getEmail());

        otpKafkaProducer.otpRequestSend(userDTO.getEmail());

        return "OTP sent to email. Please verify to complete registration.";

    }

    @Override
    @Transactional
    public String validateOTP(String email, String password, String otp) {
        log.info("Validating OTP for user: {}", email);

        // Ensure Kafka topic format is correct
        kafkaTemplate.send("otp_request", email);

        CompletableFuture<Boolean> otpFuture = new CompletableFuture<>();
        futures.put(email, otpFuture);

        try {
            Boolean isValidOTP = otpFuture.get(10, TimeUnit.SECONDS);
            if (!isValidOTP) {
                throw new CustomException("Invalid OTP or Expired");
            }
        } catch (Exception e) {
            throw new CustomException("OTP validation failed: " + e.getMessage());
        } finally {
            futures.remove(email);
        }

        // Register user after OTP validation
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        user.setMfaEnabled(true);
        userRepository.save(user);

        log.info("User registered successfully: {}", email);
        String token = jwtUtil.generateToken(email);

        return "User registered successfully. Token: " + token;
    }

    @KafkaListener(topics = "otp_request", groupId = "otp_sender")
    public void handleOtpValidationResponse(String message) {
        String[] parts = message.split(":");
        String email = parts[0];
        boolean isValid = Boolean.parseBoolean(parts[1]);


        CompletableFuture<Boolean> otpFuture = futures.get(email);
        if (otpFuture != null) {
            otpFuture.complete(isValid);
        }
    }
}
