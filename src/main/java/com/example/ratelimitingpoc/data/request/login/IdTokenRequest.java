package com.example.ratelimitingpoc.data.request.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdTokenRequest extends LoginRequest {
    private String idToken;
}
