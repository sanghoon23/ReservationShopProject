package tyml.reservationshop.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
public abstract class Oauth2UserInfo {

    protected Map<String, Object> attributes;

    public abstract String getId();
    public abstract String getEmail();
    public abstract String getName();
}
