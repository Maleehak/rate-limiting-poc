package com.example.ratelimitingpoc.strategy;

import com.example.ratelimitingpoc.data.request.login.LoginRequest;
import com.example.ratelimitingpoc.data.response.JwtTokenResponse;

public interface LoginStrategy {
    JwtTokenResponse login(LoginRequest request);
}
