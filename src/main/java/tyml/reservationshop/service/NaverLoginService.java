package tyml.reservationshop.service;

import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tyml.reservationshop.domain.dto.KakaoTokenResponseDto;
import tyml.reservationshop.domain.dto.KakaoUserInfoResponseDto;
import tyml.reservationshop.domain.dto.NaverTokenResponseDto;
import tyml.reservationshop.domain.dto.NaverUserInfoResponseDto;

@Slf4j
//@RequiredArgsConstructor
@Service
public class NaverLoginService {

    @Value("${naver.client_id}")
    private String clientId;

    @Value("${naver.redirect_uri}")
    private String redirectUri;

    @Value("${naver.client_secret}")
    private String clientSecret;

    private final String NAVER_TOKEN_URL_HOST ;
    private final String NAVER_USER_URL_HOST;
    private final String NAVER_LOGOUT_URL_HOST;


    @Autowired
    public NaverLoginService(@Value("${naver.client_id}") String clientId) {
        this.clientId = clientId;
        NAVER_TOKEN_URL_HOST ="https://nid.naver.com";
        NAVER_USER_URL_HOST = "https://openapi.naver.com";
        NAVER_LOGOUT_URL_HOST = "https://nid.naver.com";
    }

    public String getAccessTokenFromNaver(String code, String state) {

        NaverTokenResponseDto naverTokenResponseDto = WebClient.create(NAVER_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth2.0/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("code", code)
                        .queryParam("state", state)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(NaverTokenResponseDto.class)
                .block();


        log.info(" [Naver Service] Access Token ------> {}", naverTokenResponseDto.getAccessToken());
        log.info(" [Naver Service] Refresh Token ------> {}", naverTokenResponseDto.getRefreshToken());
        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
        log.info(" [Naver Service] Client Id ------> {}", naverTokenResponseDto.getClientId());
        log.info(" [Naver Service] Client Secret ------> {}", naverTokenResponseDto.getClientSecret());

        return naverTokenResponseDto.getAccessToken();
    }


    public NaverUserInfoResponseDto getUserInfo(String accessToken) {

        NaverUserInfoResponseDto userInfo = WebClient.create(NAVER_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v1/nid/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(NaverUserInfoResponseDto.class)
                .block();

        log.info("[ Naver Service ] Auth ID ---> {} ", userInfo.getNaverUserInfoResponse().getId());
        log.info("[ Naver Service ] NickName ---> {} ", userInfo.getNaverUserInfoResponse().getNickname());
        log.info("[ Naver Service ] Name ---> {} ", userInfo.getNaverUserInfoResponse().getName());
        log.info("[ Naver Service ] Email ---> {} ", userInfo.getNaverUserInfoResponse().getEmail());

        return userInfo;
    }

    public void logoutFromNaver(String accessToken) {

        WebClient.create(NAVER_LOGOUT_URL_HOST)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth2.0/token")
                        .queryParam("grant_type", "delete")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("access_token", accessToken)
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(Void.class)
                .block();

        log.info("[NAVER Service] User logged out successfully.");

    }

}
