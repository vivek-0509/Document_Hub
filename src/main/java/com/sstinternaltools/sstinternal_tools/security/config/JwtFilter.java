package com.sstinternaltools.sstinternal_tools.security.config;

import com.sstinternaltools.sstinternal_tools.security.exception.JwtAuthenticationException;
import com.sstinternaltools.sstinternal_tools.security.exception.UserNotFoundException;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.JwtService;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.MyUserDetailService;
import com.sstinternaltools.sstinternal_tools.user.entity.User;
import com.sstinternaltools.sstinternal_tools.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final MyUserDetailService myUserDetailsService;

    public JwtFilter(JwtService jwtService, UserRepository userRepository, MyUserDetailService myUserDetailsService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.myUserDetailsService = myUserDetailsService;
    }

    // List of paths that don't require authentication
    private final List<String> publicPaths = Arrays.asList(
            "/auth",
            "/api/auth/verify",
            "/oauth2",
            "/login",
            "/error",
            "/favicon.ico"
    );

    private boolean isPublicPath(String path) {
        return publicPaths.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip filtering for auth endpoints
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response); // Skip JWT check
            return;
        }

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("accessToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"No access token found\"}");
            return;
        }

        final String userEmail;

        try {
            userEmail = jwtService.extractEmail(token);
        } catch (Exception e) {

            throw new JwtAuthenticationException("Invalid JWT token.");
        }
        
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByEmailWithRoles(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));

            if (!jwtService.validateAccessToken(token, user)) {
                // Send 401 Unauthorized for invalid token, no redirect
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired access token.");
                return; // Stop filter chain
            }

            UserDetails userDetails = myUserDetailsService.loadUserByEmail(userEmail);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
