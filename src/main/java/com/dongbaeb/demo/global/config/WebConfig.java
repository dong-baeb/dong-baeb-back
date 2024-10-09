package com.dongbaeb.demo.global.config;

import com.dongbaeb.demo.global.KakaoUserInfoArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final KakaoUserInfoArgumentResolver kakaoUserInfoArgumentResolver;

    public WebConfig(KakaoUserInfoArgumentResolver kakaoUserInfoArgumentResolver) {
        this.kakaoUserInfoArgumentResolver = kakaoUserInfoArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(kakaoUserInfoArgumentResolver);
    }
}
