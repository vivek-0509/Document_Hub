package com.sstinternaltools.sstinternal_tools.security.repository;

import com.sstinternaltools.sstinternal_tools.security.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    Optional<JwtToken> findByToken(String token);
    @Query("""
           SELECT t FROM JwtToken t
           WHERE t.user.id = :userId AND (t.expired = false AND t.revoked = false)
           """)
    List<JwtToken> findAllValidTokensByUser(Long userId);

    @Query("""
           SELECT t FROM JwtToken t
           WHERE t.user.id = :userId AND t.tokenType = 'REFRESH' AND (t.expired = false AND t.revoked = false)
           """)
    List<JwtToken> findActiveRefreshTokensByUser(Long userId);

    @Query("""
           SELECT t FROM JwtToken t
           WHERE t.user.id = :userId AND (t.expired = false OR t.revoked = false)
           """)
    List<JwtToken> findAllTokensToRevoke(Long userId);

    List<JwtToken> findAllTokensByUser_Email(String email);

}