package tyml.reservationshop.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import tyml.reservationshop.domain.Address;
import tyml.reservationshop.domain.Member;

import java.util.Random;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "이름을 입력하세요.")
    private String name;

    @NotEmpty(message = "비밀번호를 입력하세요.")
    private String pw;

    private String pwCheck;

    @NotEmpty(message = "휴대전화를 입력하세요.")
    private String phoneNumber;

    @NotEmpty(message = "이메일을 입력하세요.")
    @Email
    private String email;

    private String postcodes = " ";

    private String mainAddress = " ";

    private String detailAddress = " ";

    public MemberForm() {}
    public MemberForm(Member member) {

        this.name = member.getName();
        this.phoneNumber = member.getPhoneNumber();
        this.email = member.getEmail();
        this.postcodes = member.getAddress().getPostcodes();
        this.mainAddress = member.getAddress().getMainAddress();
        this.detailAddress = member.getAddress().getDetailAddress();

    }


    /*
************************************************************************************************
     */

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
