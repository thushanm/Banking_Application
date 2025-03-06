package com.thushan.user_Service.service;

import com.thushan.user_Service.dto.UserDTO;
import com.thushan.user_Service.exceptions.CustomException;

public interface UserService {
    String register(UserDTO user) throws CustomException;
    String authenticate(String email, String password) throws CustomException;
    String generateOTP(String email) throws CustomException;
}
