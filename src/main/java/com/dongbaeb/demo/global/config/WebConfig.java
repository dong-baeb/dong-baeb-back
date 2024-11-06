package com.dongbaeb.demo.global.config;

import com.dongbaeb.demo.global.AuthenticationInterceptor;
import com.dongbaeb.demo.global.KakaoUserInfoArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final KakaoUserInfoArgumentResolver kakaoUserInfoArgumentResolver;
    private final AuthenticationInterceptor authenticationInterceptor;

    public WebConfig(KakaoUserInfoArgumentResolver kakaoUserInfoArgumentResolver, AuthenticationInterceptor authenticationInterceptor) {
        this.kakaoUserInfoArgumentResolver = kakaoUserInfoArgumentResolver;
        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(kakaoUserInfoArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/members/**");
    }
}
