package com.ams.restapi.authentication;

import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;

public class CsrfPostRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        // Enable CSRF for POST requests to /your-endpoint
        return !("/attendance".equals(request.getRequestURI()) && 
                 "POST".equalsIgnoreCase(request.getMethod()));
    }
}