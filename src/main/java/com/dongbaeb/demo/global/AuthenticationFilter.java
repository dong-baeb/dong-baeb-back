package com.dongbaeb.demo.global;

import com.dongbaeb.demo.auth.infrastructure.AccessTokenExtractor;
import com.dongbaeb.demo.auth.infrastructure.JwtTokenProvider;
import com.dongbaeb.demo.global.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final AccessTokenExtractor accessTokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwtToken = accessTokenExtractor.extractToken(request.getHeader(HttpHeaders.AUTHORIZATION));
            Long memberId = jwtTokenProvider.extractMemberId(jwtToken);
            request.setAttribute("member_id", memberId);
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            responseError(response, exception);
        }
    }

    private void responseError(HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter()
                .write(objectMapper.writeValueAsString(new ErrorResponse(exception.getMessage())));
    }
}
