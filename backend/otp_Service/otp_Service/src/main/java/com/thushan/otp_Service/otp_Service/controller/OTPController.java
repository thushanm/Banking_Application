package com.thushan.otp_Service.otp_Service.controller;

import com.thushan.otp_Service.otp_Service.dto.OTPRequestDTO;
import com.thushan.otp_Service.otp_Service.dto.OTPResponseDTO;
import com.thushan.otp_Service.otp_Service.service.OTPService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("otp")
@RequiredArgsConstructor
public class OTPController {

private final OTPService otpService;

    @PostMapping("/generate")
    public ResponseEntity<OTPResponseDTO> generateOTP(@RequestBody OTPRequestDTO otpRequestDTO) {
        return ResponseEntity.ok(otpService.genarateOTP(otpRequestDTO));
    }

    @PostMapping("/validate")
    public ResponseEntity<OTPResponseDTO> validateOTP(@RequestParam String email, @RequestParam String otp) {
        return ResponseEntity.ok(otpService.validateOTP(email, otp));
    }
}
