package tyml.reservationshop.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponseDto {

    @JsonProperty("grant_type")
    public String grantType;
    @JsonProperty("client_id")
    public String clientId;
    @JsonProperty("client_secret")
    public String clientSecret;
    @JsonProperty("code")
    public Integer code;
    @JsonProperty("state")
    public String state;
    @JsonProperty("token_type")
    public String tokenType;
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("id_token")
    public String idToken;
    @JsonProperty("expires_in")
    public Integer expiresIn;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("refresh_token_expires_in")
    public Integer refreshTokenExpiresIn;
    @JsonProperty("scope")
    public String scope;
    @JsonProperty("service_provider")
    public String serviceProvider;

}
