package com.thushan.user_Service.service;

import com.thushan.user_Service.dto.UserDTO;
import com.thushan.user_Service.exceptions.CustomException;

public interface UserService {
    String register(UserDTO user) throws CustomException;
  String validateOTP(String email, String otp,String password);

}
