package com.example.ratelimitingpoc.service;

import com.example.ratelimitingpoc.data.request.login.LoginUserRequest;
import com.example.ratelimitingpoc.data.request.registration.RegisterUserRequest;
import com.example.ratelimitingpoc.data.response.JwtTokenResponse;
import com.example.ratelimitingpoc.entity.User;
import com.example.ratelimitingpoc.enums.LoginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.ratelimitingpoc.contant.Constants.*;

@Service
public class UserAuthenticationService {
    @Autowired
     private RegistrationService registrationService;

     @Autowired
     private ApplicationEventPublisher eventPublisher;

     @Autowired
     private LoginService loginService;

     public User registerUser(RegisterUserRequest request) {
         User user = registrationService.register(request);
         Map<String, String> additional = new HashMap<>();
         additional.put(FIRST_NAME, user.getFirstname());
         additional.put(LAST_NAME, user.getLastname());
         additional.put(PHONE_NUMBER, user.getPhoneNumber());


          return user;
     }

     public JwtTokenResponse login(LoginUserRequest request) {
          JwtTokenResponse token = loginService.login(LoginType.EMAIL_PASSWORD, request);
          Optional<User> user = loginService.getByEmail(request.getEmail());

          Map<String, String> additional = new HashMap<>();
          additional.put(FIRST_NAME, user.get().getFirstname());
          additional.put(LAST_NAME, user.get().getLastname());
          additional.put(PHONE_NUMBER, user.get().getPhoneNumber());

          return token;
     }

}
