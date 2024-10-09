package com.dongbaeb.demo.auth.infrastructure;

import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfo;
import com.dongbaeb.demo.global.dto.AccessToken;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoOauthClient {
    private final String userInfoRequestUrl;
    private final RestClient restClient;

    public KakaoOauthClient(@Value("${oauth.kakao.user-info-request-url}") String userInfoRequestUrl) {
        this.userInfoRequestUrl = userInfoRequestUrl;
        this.restClient = bulidRestClient();
    }

    private RestClient bulidRestClient() {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(1))
                .withReadTimeout(Duration.ofSeconds(3));

        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactories.get(settings);

        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

    // TODO: 예외 처리 필요
    public KakaoUserInfo requestUserInfo(AccessToken accessToken) {
        return restClient.get()
                .uri(userInfoRequestUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.accessToken())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(KakaoUserInfo.class)
                .getBody();
    }
}
