package com.dongbaeb.demo.global;

import com.dongbaeb.demo.auth.infrastructure.AccessTokenExtractor;
import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AccessTokenExtractor accessTokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationInterceptor(AccessTokenExtractor accessTokenExtractor, JwtTokenProvider jwtTokenProvider) {
        this.accessTokenExtractor = accessTokenExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        String jwtToken = accessTokenExtractor.extractToken(token);
        System.out.println("토큰 정보: "+ jwtToken);

        if (token == null || !isValidToken(jwtToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("토큰이 유효하지 않습니다.");
            return false;
        }

        return true;
    }

    private boolean isValidToken(String jwtToken) {
        String issuer = jwtTokenProvider.getIssuer(jwtToken);

        if(!issuer.equals("DongBaeb")) {
            return false;
        }

        if(jwtTokenProvider.isExpired(jwtToken)) {
            return false;
        }

        return true;
    }
}
