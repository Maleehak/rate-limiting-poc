package com.example.ratelimitingpoc.utils;

import org.springframework.util.AntPathMatcher;

public class UrlPatternMatcher {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    public static final String USER_URL = "/api/v1/user/**";

    public static boolean matches(String url) {
        return pathMatcher.match(USER_URL, url);
    }
}
