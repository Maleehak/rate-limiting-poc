package com.example.ratelimitingpoc.service;

import com.example.ratelimitingpoc.data.request.login.LoginRequest;
import com.example.ratelimitingpoc.data.response.JwtTokenResponse;
import com.example.ratelimitingpoc.entity.User;
import com.example.ratelimitingpoc.enums.LoginType;
import com.example.ratelimitingpoc.repository.UserRepository;
import com.example.ratelimitingpoc.strategy.EmailAndPasswordLoginStrategy;
import com.example.ratelimitingpoc.strategy.LoginStrategy;
import com.google.common.collect.ImmutableMap;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ApplicationContext applicationContext;

    private static final ImmutableMap<LoginType, Class<? extends LoginStrategy>> loginStrategiesDefinition = ImmutableMap.of(
            LoginType.EMAIL_PASSWORD, EmailAndPasswordLoginStrategy.class
    );

    private Map<LoginType, ? extends LoginStrategy> loginStrategies;

    public JwtTokenResponse login(LoginType loginType, LoginRequest request) {
        LoginStrategy strategy = loginStrategies.get(loginType);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for login type: " + loginType);
        }
        return strategy.login(request);
    }

    @PostConstruct
    void prepareStrategies() {
        loginStrategies = prepareLoginStrategies(loginStrategiesDefinition);
    }

    private <T> Map<T, ? extends LoginStrategy> prepareLoginStrategies(Map<T, Class<? extends LoginStrategy>> definition) {
        return definition.entrySet().stream()
                .map(e -> Pair.of(e.getKey(), applicationContext.getBean(e.getValue())))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    public Optional<User> getByEmail(String email){
        return userRepository.findByEmail(email);
    }
}

