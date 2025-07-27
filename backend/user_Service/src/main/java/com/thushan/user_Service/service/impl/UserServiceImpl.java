package com.thushan.user_Service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thushan.user_Service.dto.UserDTO;
import com.thushan.user_Service.entity.User;
import com.thushan.user_Service.exceptions.CustomException;
import com.thushan.user_Service.kafka.OtpKafkaProducer;
import com.thushan.user_Service.repository.UserRepository;
import com.thushan.user_Service.security.JwtUtil;
import com.thushan.user_Service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ValueOperations<String, String> valueOperations;
    private final OtpKafkaProducer otpKafkaProducer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public String register(UserDTO userDTO) throws CustomException {
        log.info("Initiating registration of user {}", userDTO.getEmail());

        storeUserDataInRedis(userDTO);

        otpKafkaProducer.sendOtpRequest(userDTO.getEmail());
        log.info("OTP request sent to {}", userDTO.getEmail());

        return "OTP sent successfully to " + userDTO.getEmail();
    }

    private void storeUserDataInRedis(UserDTO userDTO) throws CustomException {
        try {
            String userJson = objectMapper.writeValueAsString(userDTO);
            valueOperations.set("pending_userData:" + userDTO.getEmail(), userJson, 10, TimeUnit.MINUTES);
            log.info("User data stored in Redis for {}", userDTO.getEmail());
        } catch (JsonProcessingException e) {
            throw new CustomException("Failed to store user data in Redis");
        }
    }

    @Override
    @Transactional
    public void saveUserToDatabase(String email) throws CustomException {
        String redisKey = "pending_userData:" + email;
        String storedUserJson = valueOperations.get(redisKey);

        if (storedUserJson == null) {
            log.error("No pending user data found in Redis for key: {}", redisKey);
            throw new CustomException("User data expired or missing. Please restart registration.");
        }

        try {
            log.info("User data retrieved from Redis for email: {}", email); // Debugging log
            UserDTO storedUserDTO = objectMapper.readValue(storedUserJson, UserDTO.class);

            log.info("UserDTO: {}", storedUserDTO);

            User user = new User();
            user.setEmail(storedUserDTO.getEmail());
            user.setPassword(passwordEncoder.encode(storedUserDTO.getPassword()));
            user.setRole(storedUserDTO.getRole());
            user.setMfaEnabled(true);

            log.info("Saving user to database: {}", user);
            userRepository.save(user);
            log.info("User saved to database successfully: {}", user.getEmail());
            otpKafkaProducer.sendUserId(user.getId());
            valueOperations.getOperations().delete(redisKey);
        } catch (Exception e) {
            log.error("Failed to save user to database", e);
            throw new CustomException("Failed to save user to database: " + e.getMessage());
        }
    }


    @Override
    public String getOtpFromRedis(String email) {
        return valueOperations.get("otp:" + email);
    }

    @Override
    public void sendOtpValidationResult(String email, boolean result) {
        String validationResult = email + ":" + result;
        otpKafkaProducer.sendOtpValidationResult(validationResult);
    }
}
