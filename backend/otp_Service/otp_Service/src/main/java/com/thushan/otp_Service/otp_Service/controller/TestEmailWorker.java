package com.thushan.otp_Service.otp_Service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestEmailWorker {
    private final JavaMailSender mailSender;

    @GetMapping
    public ResponseEntity<String> sendTestEmail() {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo("thushanmadhushara2@gmail.com");
            mailMessage.setSubject("Test Email");
            mailMessage.setText("This is a test email from Spring Boot.");

            mailSender.send(mailMessage);
            return ResponseEntity.ok(" Test email sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(" Email failed: " + e.getMessage());
        }
    }

}
