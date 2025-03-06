package com.thushan.authentication_Service.controller;

import com.thushan.authentication_Service.entity.User;
import com.thushan.authentication_Service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class AuthenticationController {
    @Autowired
    private  AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<String> registers(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.register(user));
    }
}
