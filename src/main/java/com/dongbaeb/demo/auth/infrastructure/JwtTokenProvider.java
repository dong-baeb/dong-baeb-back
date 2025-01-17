package com.dongbaeb.demo.auth.infrastructure;

import com.dongbaeb.demo.global.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private static final String MEMBER_ID_KEY = "member_id";
    private final String issuer;
    private final Long accessExpiration;
    private final SecretKey secretKey;

    public JwtTokenProvider(
            @Value("${application.name}") String issuer,
            @Value("${jwt.access-expiration}") Long accessExpiration,
            @Value("${jwt.secret-key}") String secretKey) {
        this.issuer = issuer;
        this.accessExpiration = accessExpiration;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createAccessToken(Long memberId) {
        Date now = new Date();

        return Jwts.builder()
                .claim(MEMBER_ID_KEY, memberId)
                .issuer(issuer)
                .expiration(new Date(now.getTime() + accessExpiration))
                .signWith(secretKey)
                .compact();
    }

    public Long extractMemberId(String jwtToken) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload()
                    .get(MEMBER_ID_KEY, Long.class);
        } catch (IllegalArgumentException exception) {
            throw new UnauthorizedException("JWT 토큰이 비어있습니다.");
        } catch (ExpiredJwtException exception) {
            throw new UnauthorizedException("JWT 토큰이 만료되었습니다.");
        } catch (Exception exception) {
            throw new UnauthorizedException("유효하지 않은 JWT 토큰입니다.");
        }
    }
}
