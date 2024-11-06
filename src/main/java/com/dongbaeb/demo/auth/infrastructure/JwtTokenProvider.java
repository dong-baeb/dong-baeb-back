package com.dongbaeb.demo.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
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
                .claim("member_id", memberId)
                .issuer(issuer)
                .expiration(new Date(now.getTime() + accessExpiration))
                .signWith(secretKey)
                .compact();
    }

    public Boolean isExpired(String jwtToken) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (SignatureException e) {
            return true;
        } catch (JwtException e) {
            return true;
        }
    }

    public String getIssuer(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("iss", String.class);
        } catch (SignatureException e) {
            return "Signature가 올바르지 않습니다.";
        } catch (JwtException e) {
            return "Jwt 예외 발생";
        }
    }
}
