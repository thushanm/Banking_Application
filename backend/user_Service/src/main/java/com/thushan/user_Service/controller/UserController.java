package com.thushan.user_Service.controller;

import com.thushan.user_Service.dto.OTPResponse;
import com.thushan.user_Service.dto.UserDTO;
import com.thushan.user_Service.exceptions.CustomException;
import com.thushan.user_Service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        try {
            String response = userService.register(userDTO);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateOtp(@RequestBody OTPResponse otpResponse) {
        String storedOtp = userService.getOtpFromRedis(otpResponse.getEmail());

        if (storedOtp != null && storedOtp.equals(otpResponse.getOtp())) {
            try {

                userService.saveUserToDatabase(otpResponse.getEmail());
                return ResponseEntity.ok("OTP is valid. User registered successfully.");
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body("Error saving user to the database: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(401).body("Invalid or expired OTP.");
        }
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
