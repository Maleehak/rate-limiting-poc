package com.example.ratelimitingpoc.data.response;

import lombok.Builder;

@Builder
public record JwtTokenResponse(
        String token,
        Long expiresIn
){ }
