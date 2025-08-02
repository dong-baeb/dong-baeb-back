package com.dongbaeb.demo.auth.infrastructure;

import com.dongbaeb.demo.auth.dto.kakao.KakaoUserInfo;
import com.dongbaeb.demo.global.dto.AccessToken;
import com.dongbaeb.demo.global.exception.BadRequestException;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
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
        try {
            return restClient.get()
                    .uri(userInfoRequestUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.accessToken())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(KakaoUserInfo.class)
                    .getBody();
        } catch (WebClientResponseException e) {
            // HTTP 오류 응답 처리 (4xx, 5xx)
            log.error("Kakao API 응답 오류: statusCode={}, responseBody={}", e.getStatusCode(), e.getResponseBodyAsString(),
                    e);
            throw new BadRequestException("카카오 사용자 정보 요청 실패: 응답 오류");
        } catch (WebClientRequestException e) {
            // 요청 자체 실패 (네트워크 오류 등)
            log.error("Kakao API 요청 실패", e);
            throw new BadRequestException("카카오 사용자 정보 요청 실패: 네트워크 오류");
        } catch (Exception e) {
            // 그 외 모든 예외
            log.error("Kakao 사용자 정보 요청 중 알 수 없는 오류 발생", e);
            throw new BadRequestException("카카오 사용자 정보 요청 실패: 알 수 없는 오류");
        }
    }
}
