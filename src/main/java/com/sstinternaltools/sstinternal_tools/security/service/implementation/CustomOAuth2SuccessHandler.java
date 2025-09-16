package com.sstinternaltools.sstinternal_tools.security.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sstinternaltools.sstinternal_tools.security.exception.InvalidCredentialsException;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.AuthService;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.CustomLogicService;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.JwtService;
import com.sstinternaltools.sstinternal_tools.user.entity.User;
import com.sstinternaltools.sstinternal_tools.user.entity.UserRole;
import com.sstinternaltools.sstinternal_tools.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CustomLogicService customLogicService;
    private final AuthService authService;
    private final String frontendUrl;

    public CustomOAuth2SuccessHandler(JwtService jwtService, UserRepository userRepository, CustomLogicService customLogicService, AuthService authService,@Value("${FRONTEND_URL}") String frontendUrl) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.customLogicService = customLogicService;
        this.authService = authService;
        this.frontendUrl = frontendUrl;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            String domain = email.split("@")[1];

            if (!domain.equals("sst.scaler.com") && !domain.equals("scaler.com")) {
                throw new InvalidCredentialsException("Invalid domain: " + domain);
            }

            if (userRepository.findByEmail(email).isEmpty()) {

                authService.register(email);

                List<UserRole> roles = customLogicService.assignRoles(email);

                if (roles.isEmpty()) {
                    throw new InvalidCredentialsException("You are not allowed to access");
                }

                User user = userRepository.findByEmail(email).get();
                user.setUserRoles(roles);
                userRepository.save(user);
            }

            ResponseCookie accessCookie = jwtService.generateAccessCookie(email);
            ResponseCookie refreshCookie = jwtService.generateRefreshCookie(email);

            response.addHeader("Set-Cookie", accessCookie.toString());
            response.addHeader("Set-Cookie", refreshCookie.toString());

            String redirectUrl = frontendUrl + "/auth/callback";
            response.sendRedirect(redirectUrl);



        } catch(Exception e){
            response.sendRedirect(frontendUrl + "/login?error=" +"OAuth2 login failed. Please try again.");
        }
    }
}