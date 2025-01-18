package com.dongbaeb.demo.global;

import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfo;
import com.dongbaeb.demo.auth.infrastructure.AccessTokenExtractor;
import com.dongbaeb.demo.auth.infrastructure.KakaoOauthClient;
import com.dongbaeb.demo.global.dto.AccessToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class KakaoUserInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final AccessTokenExtractor accessTokenExtractor;
    private final KakaoOauthClient kakaoOauthClient;

    public KakaoUserInfoArgumentResolver(AccessTokenExtractor accessTokenExtractor, KakaoOauthClient kakaoOauthClient) {
        this.accessTokenExtractor = accessTokenExtractor;
        this.kakaoOauthClient = kakaoOauthClient;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType()
                .equals(KakaoUserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String kakaoAccessToken = accessTokenExtractor.extractToken(authorizationHeader);

        return kakaoOauthClient.requestUserInfo(new AccessToken(kakaoAccessToken));
    }
}
