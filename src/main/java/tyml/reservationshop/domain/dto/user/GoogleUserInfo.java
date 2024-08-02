package tyml.reservationshop.domain.dto.user;

import lombok.Getter;

import java.util.Map;

@Getter
public class GoogleUserInfo extends Oauth2UserInfo{

    public GoogleUserInfo(Map<String, Object> attributes) {
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
        return attributes.get("name").toString();
    }

}
