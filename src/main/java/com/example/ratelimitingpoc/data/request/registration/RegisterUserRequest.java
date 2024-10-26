package com.example.ratelimitingpoc.data.request.registration;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {
    @NotEmpty(message = "First Name is required")
    String firstName;
    @NotEmpty(message = "Last Name is required")
    String lastName;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email is required")
    String email;
    @Size(min=10,max=15)
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number")
    @NotEmpty(message = "Phone number is required")
    String phoneNumber;
    @NotEmpty(message = "Password is required")
    String password;
    String role;
}
