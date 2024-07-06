package tyml.reservationshop.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverUserInfoResponseDto {

    @JsonProperty("resultcode")
    private String resultcode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("response")
    private NaverUserInfoResponse naverUserInfoResponse;

    // Getter, Setter 생략

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class NaverUserInfoResponse {

        @JsonProperty("id")
        private String id;

        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private String email;

        @JsonProperty("gender")
        private String gender;

        @JsonProperty("age")
        private String age;

        @JsonProperty("birthday")
        private String birthday;

        @JsonProperty("profile_image")
        private String profileImage;

        @JsonProperty("birthyear")
        private String birthyear;

        @JsonProperty("mobile")
        private String mobile;
    }
}