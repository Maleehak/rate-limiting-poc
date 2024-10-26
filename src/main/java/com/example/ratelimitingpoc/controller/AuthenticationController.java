package com.example.ratelimitingpoc.controller;

import com.example.ratelimitingpoc.data.request.login.IdTokenRequest;
import com.example.ratelimitingpoc.data.request.login.LoginUserRequest;
import com.example.ratelimitingpoc.data.request.registration.RegisterUserRequest;
import com.example.ratelimitingpoc.data.response.JwtTokenResponse;
import com.example.ratelimitingpoc.entity.User;
import com.example.ratelimitingpoc.service.JwtService;
import com.example.ratelimitingpoc.service.UserAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    UserAuthenticationService userAuthenticationService;

    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterUserRequest request) {
        User user = userAuthenticationService.registerUser(request);
        return "Registered successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> login(@RequestBody LoginUserRequest request) {
        return ResponseEntity.ok(userAuthenticationService.login(request));
    }
}
