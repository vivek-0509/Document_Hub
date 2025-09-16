package com.sstinternaltools.sstinternal_tools.security.service.implementation;

import com.sstinternaltools.sstinternal_tools.security.exception.JwtAuthenticationException;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.AuthService;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.JwtService;
import com.sstinternaltools.sstinternal_tools.user.entity.User;
import com.sstinternaltools.sstinternal_tools.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthServiceImpl(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public void rotateRefreshToken(String refreshToken, HttpServletResponse response) {

        if (refreshToken == null) {
            throw new JwtAuthenticationException("Invalid refresh token");
        }

        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new JwtAuthenticationException("Expired or revoked refresh token");
        }

        String email = jwtService.extractEmail(refreshToken);
        jwtService.revokeRefreshToken(refreshToken);

        ResponseCookie newAccessToken = jwtService.generateAccessCookie(email);
        ResponseCookie newRefreshToken = jwtService.generateRefreshCookie(email);

        response.addHeader("Set-Cookie", newAccessToken.toString());
        response.addHeader("Set-Cookie", newRefreshToken.toString());

    }

    @Override
    public void logout(String refreshToken) {

        if (refreshToken == null) {
            throw new JwtAuthenticationException("Invalid refresh token");
        }

        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new JwtAuthenticationException("Expired or revoked refresh token");
        }

        jwtService.revokeRefreshToken(refreshToken);
    }

    @Override
    @Transactional
    public void register(String email) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(email.substring(0, email.indexOf('.')));
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<?> verifyAuth(HttpServletRequest request) {
        System.out.println("Verify endpoint called");

        // First check cookies for JWT token
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println("Found " + cookies.length + " cookies");
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    try {
                        String token = cookie.getValue();
                        String email = jwtService.extractEmail(token);
                        System.out.println("Found JWT token for email: " + email);

                        Optional<User> userOpt = userRepository.findByEmail(email);
                        if (userOpt.isPresent() && jwtService.validateAccessToken(token, userOpt.get())) {
                            Map<String, Object> response = new HashMap<>();
                            response.put("authenticated", true);
                            response.put("email", email);
                            System.out.println("JWT authentication successful");
                            return ResponseEntity.ok(response);
                        }
                    } catch (Exception e) {
                        System.out.println("Error validating JWT: " + e.getMessage());
                    }
                }
            }
        } else {
            System.out.println("No cookies found in request");
        }

        // Then check for OAuth2 authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Checking OAuth2 authentication: " + authentication);

        if (authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {

            Object principal = authentication.getPrincipal();
            String email = null;

            if (principal instanceof OAuth2User) {
                email = ((OAuth2User) principal).getAttribute("email");
            } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
                email = (String) principal;
            }

            if (email != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("authenticated", true);
                response.put("email", email);
                System.out.println("OAuth2 authentication successful");
                return ResponseEntity.ok(response);
            }
        }

        System.out.println("Authentication failed");
        return ResponseEntity.status(401).body(Map.of(
                "authenticated", false,
                "message", "No valid authentication found"
        ));
    }

}