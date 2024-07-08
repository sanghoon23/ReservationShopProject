package tyml.reservationshop.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "")
    private String name;

    @NotEmpty(message = "")
    private String createId;

    @NotEmpty(message = "")
    private String pw;

    private String pwCheck;

    @NotEmpty(message = "")
    private String phoneNumber;

    @NotEmpty(message = "")
    private String email;

    @NotEmpty(message = "")
    private String postcodes;

    @NotEmpty(message = "")
    private String address;

    @NotEmpty(message = "")
    private String detailAddress;

    public static String generateRandomId() {
        int length = new Random().nextInt(3) + 5; // 5에서 7 사이의 길이 선택
        StringBuilder id = new StringBuilder(length);
        Random random = new Random();

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            id.append(characters.charAt(index));
        }

        return id.toString();
    }

}
