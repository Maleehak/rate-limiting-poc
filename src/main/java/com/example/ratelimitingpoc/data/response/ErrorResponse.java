package com.example.ratelimitingpoc.data.response;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String message
){ }
