package com.dongbaeb.demo.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")             // 허용할 엔드포인트
                .allowedOrigins("http://localhost:3000") // Flutter 웹 개발용 주소
                .allowedMethods("*")                     // 허용 HTTP 메서드 (preflight)
                .allowedHeaders("*")                     // 헤더 포함 허용
                .allowCredentials(true);                 // 쿠키/인증 허용
    }
}