package com.thushan.otp_Service.otp_Service.service;

import com.thushan.otp_Service.otp_Service.dto.OTPRequestDTO;
import com.thushan.otp_Service.otp_Service.dto.OTPResponseDTO;

public interface OTPService {
    OTPResponseDTO genarateOTP(OTPRequestDTO otpResponseDTO);
    OTPResponseDTO validateOTP(String email,String otp);



}
