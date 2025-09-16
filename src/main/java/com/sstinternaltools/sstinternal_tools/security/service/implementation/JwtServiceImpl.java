package com.sstinternaltools.sstinternal_tools.security.service.implementation;

import com.sstinternaltools.sstinternal_tools.security.entity.JwtToken;
import com.sstinternaltools.sstinternal_tools.security.entity.TokenType;
import com.sstinternaltools.sstinternal_tools.security.exception.JwtAuthenticationException;
import com.sstinternaltools.sstinternal_tools.security.repository.JwtTokenRepository;
import com.sstinternaltools.sstinternal_tools.security.service.interfaces.JwtService;
import com.sstinternaltools.sstinternal_tools.user.entity.User;
import com.sstinternaltools.sstinternal_tools.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private final String jwtSecretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final JwtTokenRepository jwtTokenRepository;
    private final UserRepository userRepository;

    public JwtServiceImpl(JwtTokenRepository jwtTokenRepository, UserRepository userRepository, @Value("${JWT_SECRET_KEY}") String jwtSecretKey, @Value("${JWT_ACCESS_EXPIRATION_TIME}") long accessTokenExpiration, @Value("${JWT_REFRESH_EXPIRATION_TIME}") long refreshTokenExpiration) {
        this.jwtTokenRepository = jwtTokenRepository;
        this.userRepository = userRepository;
        this.jwtSecretKey=jwtSecretKey;
        this.accessTokenExpiration=accessTokenExpiration;
        this.refreshTokenExpiration=refreshTokenExpiration;
    }

    //generates encoded secret key to sign the tokens
    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    //method to generate access token
    public ResponseCookie generateAccessCookie(String email) {

        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found."));


        List<String> roleNames = user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().name())
                .toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", roleNames);
        claims.put("tokenType", TokenType.ACCESS.name());
        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+accessTokenExpiration))
                .and()
                .signWith(getKey())
                .compact();

        //Added the access token in httpOnly cookie
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)                  // prevent JS access — security best practice
                .secure(false)                    // send only over HTTPS
                .path("/")                       // cookie is valid for entire app
                .maxAge(accessTokenExpiration/1000)
                .sameSite("Lax")                // prevent CSRF, still allow POST from same site
                .build();

        return accessCookie;
    }

    //method to generate refresh token
    public ResponseCookie generateRefreshCookie(String email) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User with email " + email + " not found."));

        String token = Jwts.builder()
                .claims()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+refreshTokenExpiration))
                .and()
                .signWith(getKey())
                .compact();

        JwtToken refreshToken= new JwtToken(
                token,
                TokenType.REFRESH,
                false,
                false,
                LocalDateTime.now().plusSeconds(refreshTokenExpiration/1000),
                user
        );
        jwtTokenRepository.save(refreshToken);

        //Added the refresh token in httpOnly cookie
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", token)
                .httpOnly(true)                  // prevent JS access — security best practice
                .secure(false)                    // send only over HTTPS
                .path("/")
                .maxAge((int)refreshTokenExpiration/1000)    // expires in 7 days
                .sameSite("Lax")                // prevent CSRF, still allow POST from same site
                .build();

        return refreshCookie;
    }

    //method to extract the email stored inside jwt
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims =extractAllClaims(token); //now claim holds all the data inside the Jwt
        return claimsResolver.apply(claims); //we apply claim resolver function to extract one specific claim
    }

    //generic method to extract claims from the token
    private Claims extractAllClaims(String token) {
//        System.out.println("Extracting claims from token: " + token);
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.security.SignatureException ex) {
//            System.err.println("Signature verification failed: " + ex.getMessage());
            throw new JwtAuthenticationException("Invalid JWT signature");
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
//            System.err.println("Token expired: " + ex.getMessage());
            throw new JwtAuthenticationException("JWT token has expired");
        } catch (Exception ex) {
//            System.err.println("General JWT parsing error: " + ex.getMessage());
            throw new JwtAuthenticationException("Error extracting claims from JWT");
        }
    }

    //method to validate refresh token
    public boolean isRefreshTokenValid(String token){
        return jwtTokenRepository.findByToken(token)
                .filter(t-> !t.isRevoked() && !t.isExpired() && t.getTokenType()==TokenType.REFRESH)
                .filter(t->t.getExpirationDateTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    //method to revoke Refresh token
    public void revokeRefreshToken(String token){
        jwtTokenRepository.findByToken(token).ifPresent(
                t->{
                    t.setRevoked(true);
                    jwtTokenRepository.save(t);
                }
        );
    }

    //method to revoke all the refresh tokens for the user
    public void revokeAllTokensForUser(String email){
        List<JwtToken> tokens=jwtTokenRepository.findAllTokensByUser_Email(email);
        tokens.forEach(token-> token.setRevoked(true));
        jwtTokenRepository.saveAll(tokens);
    }

    //method to validate access token
    public boolean validateAccessToken(String token, User user) {
        final String email = extractEmail(token);
        final String tokenType = extractClaim(token, claims -> claims.get("tokenType", String.class));
        return (email.equals(user.getEmail())
                && !isTokenExpired(token)
                && TokenType.ACCESS.name().equals(tokenType));
    }

    //method to check the token is expired or not
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //method to extract expiration time from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}