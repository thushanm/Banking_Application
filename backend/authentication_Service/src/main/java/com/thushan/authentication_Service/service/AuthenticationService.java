package com.thushan.authentication_Service.service;

import com.thushan.authentication_Service.entity.User;
import com.thushan.authentication_Service.exception.CustomException;
import com.thushan.authentication_Service.repository.UserRepository;
import com.thushan.authentication_Service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final JwtUtil jwtUtil;
    @Transactional
    public String register(User user) {
        System.out.println("Registering user: " + user.getEmail());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            System.out.println("User with this email already exists.");
            throw new CustomException("User with this email already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            System.out.println("Saving user to the database...");
            userRepository.save(user);
            System.out.println("User saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
            throw new CustomException("Error saving user: " + e.getMessage());
        }
        return "User registered successfully";
    }
public String authenticate(String email,String password){
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("user not found"));
        if (!passwordEncoder.matches(password,user.getPassword())){
            throw new RuntimeException("wrong Email Or password");
        }
        return jwtUtil.genarateToken(email);
}
public String genarateOTP(String email){

        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("user not found"));
        String otp = String.valueOf(new Random().nextInt(900000)+100000);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        sendOTPEmail(email,otp);
        return "OTP generated successfully";
}
private void sendOTPEmail(String email,String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OTP generated successfully");
        message.setText("OTP Is: "+otp);
        mailSender.send(message);
}

}
