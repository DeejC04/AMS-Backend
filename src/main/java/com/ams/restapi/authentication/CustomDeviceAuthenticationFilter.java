package com.ams.restapi.authentication;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomDeviceAuthenticationFilter extends OncePerRequestFilter {

    private final DeviceService deviceService;

    public CustomDeviceAuthenticationFilter(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("X-Custom-Token");

        if (("/attendance".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) 
        || ("/readers".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod()))) {
            if (!deviceService.validateDevice(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
