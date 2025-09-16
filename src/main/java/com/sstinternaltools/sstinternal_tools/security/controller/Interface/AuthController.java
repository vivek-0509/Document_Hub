package com.sstinternaltools.sstinternal_tools.security.controller.Interface;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface AuthController {
    ResponseEntity<String> rotateRefreshToken (String refreshToken, HttpServletResponse response);
    ResponseEntity<String> logout(String refreshToken);
    ResponseEntity<?> verify(HttpServletRequest request);
}