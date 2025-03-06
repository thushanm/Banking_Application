package com.thushan.user_Service.service.impl;

import com.thushan.user_Service.dto.UserDTO;
import com.thushan.user_Service.entity.User;
import com.thushan.user_Service.exceptions.CustomException;
import com.thushan.user_Service.repository.UserRepository;
import com.thushan.user_Service.security.JwtUtil;
import com.thushan.user_Service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public String register(UserDTO userDTO) throws CustomException {
        log.info("Registering user: {}", userDTO.getEmail());

        if (userDTO.getEmail() == null || userDTO.getPassword() == null) {
            throw new CustomException("Email and password cannot be null.");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new CustomException("User with email " + userDTO.getEmail() + " already exists.");
        }

        // Convert UserDTO to User entity
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        try {
            log.info("Saving user to the database...");
            userRepository.save(user);
            log.info("User saved successfully.");
            return "User registered successfully.";
        } catch (Exception e) {
            log.error("Error occurred while saving user: {}", e.getMessage());
            throw new CustomException("Something went wrong: " + e.getMessage());
        }
    }

    @Override
    public String authenticate(String email, String password) throws CustomException {
        log.info("Authenticating user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException("Incorrect password.");
        }

        return jwtUtil.generateToken(email);
    }

    @Override
    @Transactional
    public String generateOTP(String email) throws CustomException {
        log.info("Generating OTP for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found with email: " + email));

        String otp = generateSecureOTP();
        user.setOtp(otp);
        user.setOtpExpiryDate(LocalDateTime.now().plusMinutes(5));

        try {
            userRepository.save(user);
            sendOTPEmail(email, otp);
            log.info("OTP generated and sent successfully.");
            return "OTP generated and sent successfully.";
        } catch (Exception e) {
            log.error("Error occurred while generating OTP: {}", e.getMessage());
            throw new CustomException("Failed to generate OTP: " + e.getMessage());
        }
    }

    private String generateSecureOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void sendOTPEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + ". It will expire in 5 minutes.");

        try {
            mailSender.send(message);
            log.info("OTP email sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send OTP email: {}", e.getMessage());
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }
}
