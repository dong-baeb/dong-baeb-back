package com.dongbaeb.demo.global.config;

import com.dongbaeb.demo.global.KakaoUserInfoArgumentResolver;
import com.dongbaeb.demo.global.MemberAuthArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final KakaoUserInfoArgumentResolver kakaoUserInfoArgumentResolver;
    private final MemberAuthArgumentResolver memberAuthArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(List.of(kakaoUserInfoArgumentResolver, memberAuthArgumentResolver));
    }

}
