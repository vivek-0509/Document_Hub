package com.sstinternaltools.sstinternal_tools.security.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    void rotateRefreshToken(String refreshToken, HttpServletResponse response);
    void logout(String refreshToken);
    void register(String email);
    public ResponseEntity<?> verifyAuth(HttpServletRequest request);
}