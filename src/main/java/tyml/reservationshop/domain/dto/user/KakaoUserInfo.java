package tyml.reservationshop.domain.dto.user;

import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoUserInfo extends Oauth2UserInfo {

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    public String getName() {
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");
        return profile.get("nickname").toString();
    }

}
