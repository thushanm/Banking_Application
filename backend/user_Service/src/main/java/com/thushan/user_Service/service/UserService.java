package com.thushan.user_Service.service;

import com.thushan.user_Service.dto.UserDTO;
import com.thushan.user_Service.exceptions.CustomException;

import java.util.List;

public interface UserService {
    String register(UserDTO userDTO) throws CustomException;
    String getOtpFromRedis(String email);
    void sendOtpValidationResult(String email, boolean result);
    void saveUserToDatabase(String email) throws CustomException;
    List<UserDTO> getAllUsers();
}
