package com.dongbaeb.demo.auth.infrastructure;

import com.dongbaeb.demo.auth.dto.kakao.KakaoAccessTokenResponse;
import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Component
public class KakaoOauthClient {
    private final String appKey;
    private final String redirectUrl;
    private final String accessTokenRequestUrl;
    private final String userInfoRequestUrl;
    private final RestClient restClient;

    public KakaoOauthClient(
            @Value("${oauth.kakao.app-key}") String appKey,
            @Value("${oauth.kakao.redirect-url}") String redirectUrl,
            @Value("${oauth.kakao.access-token-request-url}") String accessTokenRequestUrl,
            @Value("${oauth.kakao.user-info-request-url}") String userInfoRequestUrl) {
        this.appKey = appKey;
        this.redirectUrl = redirectUrl;
        this.accessTokenRequestUrl = accessTokenRequestUrl;
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

    public String requestAccessToken(String authorizationCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", appKey);
        params.add("redirect_uri", redirectUrl);
        params.add("grant_type", "authorization_code");

        return restClient.post()
                .uri(accessTokenRequestUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
//                .onStatus(HttpStatusCode::isError, this::handleClientError)
                .toEntity(KakaoAccessTokenResponse.class)
                .getBody()
                .accessToken();
    }

    public KakaoUserInfoResponse requestUserInfo(String accessToken) {
        return restClient.get()
                .uri(userInfoRequestUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(KakaoUserInfoResponse.class)
                .getBody();
    }
}
