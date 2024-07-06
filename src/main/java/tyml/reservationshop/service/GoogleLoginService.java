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
import reactor.core.publisher.Mono;
import tyml.reservationshop.domain.dto.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleLoginService {

    private String clientId;
    private String redirectUri;
    private String clientSecret;
    private final String GOOGLE_TOKEN_URL_HOST ;
    private final String GOOGLE_USER_URL_HOST ;
    private final String GOOGLE_LOGOUT_URL_HOST ;

    @Autowired
    public GoogleLoginService(@Value("${google.client_id}") String clientId,
                              @Value("${google.redirect_uri}")  String redirectUri,
                              @Value("${google.client_secret}")  String clientSecret,
                              @Value("${google.scope}") String scope) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        GOOGLE_TOKEN_URL_HOST ="https://oauth2.googleapis.com";
        GOOGLE_USER_URL_HOST ="https://www.googleapis.com";
        GOOGLE_LOGOUT_URL_HOST = "https://oauth2.googleapis.com";
    }

    public String getAccessTokenFromGoogle(String code) {

        //state 랜덤값 생성
        String state = UUID.randomUUID().toString();

        GoogleTokenResponseDto googleTokenResponseDto = WebClient.create(GOOGLE_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/token")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(GoogleTokenResponseDto.class)
                .block();


        log.info(" [Goggle Service] Access Token ------> {}", googleTokenResponseDto.getAccessToken());
        log.info(" [Goggle Service] Refresh Token ------> {}", googleTokenResponseDto.getRefreshToken());
        log.info(" [Goggle Service] Scope ------> {}", googleTokenResponseDto.getScope());

        return googleTokenResponseDto.getAccessToken();
    }

    public GoogleUserInfoResponseDto getUserInfo(String accessToken) {
        //TODO : USERINFO 구현해야함.
        GoogleUserInfoResponseDto userInfo = WebClient.create(GOOGLE_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth2/v3/userinfo")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(GoogleUserInfoResponseDto.class)
                .block();

        log.info("[ Google Service ] Auth ID ---> {} ", userInfo.getId());
        log.info("[ Google Service ] NickName ---> {} ", userInfo.getName());
        log.info("[ Google Service ] Email ---> {} ", userInfo.getEmail());

        return userInfo;
    }

    public void logoutFromGoogle(String accessToken) {

        //Revoke
        WebClient.create(GOOGLE_LOGOUT_URL_HOST)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/revoke")
                        .queryParam("token", accessToken)
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(Void.class)
                .block();

        log.info("[Google Service] User logged out successfully.");
    }

}
