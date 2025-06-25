package com.dongbaeb.demo.global.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApiLoggingFilter extends OncePerRequestFilter {
    private static final int MAX_BODY_LENGTH = 500;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        long startTime = System.currentTimeMillis();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUrl = uri + (queryString != null ? "?" + queryString : "");

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } catch (Exception ex) {
            log.error("예외 발생 - [{} {}] : {}", method, fullUrl, ex.getMessage(), ex);
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = wrappedResponse.getStatus();

            String requestBody = getContentAsString(wrappedRequest.getContentAsByteArray());
            String responseBody = getContentAsString(wrappedResponse.getContentAsByteArray());

            log.info("api 요청: [{}] {}", method, fullUrl);
            log.debug("요청 Body: {}", requestBody);

            if (status >= 500) {
                log.error("서버 오류 - 상태 코드: {}, 처리 시간: {}ms", status, duration);
            } else if (status >= 400) {
                log.warn("클라이언트 오류 - 상태 코드: {}, 처리 시간: {}ms", status, duration);
            } else {
                log.info("응답 성공 - 상태 코드: {}, 처리 시간: {}ms", status, duration);
            }

            log.debug("응답 Body: {}", responseBody);

            wrappedResponse.copyBodyToResponse();
        }
    }

    private String getContentAsString(byte[] buffer) {
        if (buffer == null || buffer.length == 0) {
            return "[empty]";
        }

        String body = new String(buffer, StandardCharsets.UTF_8);
        if (body.length() > MAX_BODY_LENGTH) {
            return body.substring(0, MAX_BODY_LENGTH) + "... [생략됨 body.length=" + body.length() + "]";
        }
        return body;
    }
}
