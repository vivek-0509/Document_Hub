package com.sstinternaltools.sstinternal_tools.security.controller.Implementation;

import com.sstinternaltools.sstinternal_tools.security.controller.Interface.AuthController;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<String> rotateRefreshToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        authService.rotateRefreshToken(refreshToken, response);
        return ResponseEntity.ok("Token refreshed successfully");
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue("refreshToken") String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok("✅ User logged out successfully");
    }

    @Override
    @GetMapping("/verify")
    public ResponseEntity<?> verify(HttpServletRequest request) {
        return authService.verifyAuth(request);
    }

}
