package tyml.reservationshop.service.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tyml.reservationshop.service.GoogleUserService;
import tyml.reservationshop.service.KakaoUserService;
import tyml.reservationshop.service.NaverUserService;
import tyml.reservationshop.service.base.userService;

@Getter
@Component
@RequiredArgsConstructor
public class UserServiceFactory {

    private final GoogleUserService googleUserService;
    private final KakaoUserService kakaoUserService;
    private final NaverUserService naverUserService;

    public userService getUserService(String provider) {

        switch (provider) {

            case "google" :
                return googleUserService;

            case "kakao" :
                return kakaoUserService;

            case "naver" :
                return naverUserService;

            default:
                throw new IllegalArgumentException("Unsupported provider : " + provider);
        }
    }

    public userService getUserServiceContainString(String provider) {

        if (provider.contains("google")) {
            return googleUserService;
        }
        else if (provider.contains("kakao")) {
            return kakaoUserService;
        }
        else if (provider.contains("naver")) {
            return naverUserService;
        }

        throw new IllegalArgumentException("Unsupported provider : " + provider);
    }


    public String getUserName(String provider, String accessToken) {

        switch (provider) {

            case "google" :
                return googleUserService.getUserInfo(accessToken).getName();

            case "kakao" :
                return kakaoUserService.getUserInfo(accessToken).getKakaoAccount().getProfile().getNickName();

            case "naver" :
                return naverUserService.getUserInfo(accessToken).getNaverUserInfoResponse().getName();

            default:
                throw new IllegalArgumentException("Unsupported provider : " + provider);
        }

    }

    public String getUserEmail(String provider, String accessToken) {

        switch (provider) {

            case "google" :
                return googleUserService.getUserInfo(accessToken).getEmail();

            case "kakao" :
                return kakaoUserService.getUserInfo(accessToken).getKakaoAccount().email;

            case "naver" :
                return naverUserService.getUserInfo(accessToken).getNaverUserInfoResponse().getEmail();

            default:
                throw new IllegalArgumentException("Unsupported provider : " + provider);
        }

    }

}
