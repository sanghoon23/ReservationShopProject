package tyml.reservationshop.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import tyml.reservationshop.domain.Member;

@Getter
@Setter
public class MemberModifyForm {

    @NotEmpty(message = "이름을 입력하세요.")
    private String name;

    @NotEmpty(message = "휴대전화를 입력하세요.")
    private String phoneNumber;

    private String email;

    private String postcodes = " ";

    private String mainAddress = " ";

    private String detailAddress = " ";

    public MemberModifyForm(){
    }

    public MemberModifyForm(Member member) {

        this.name = member.getName();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();
        this.postcodes = member.getAddress().getPostcodes();
        this.mainAddress = member.getAddress().getMainAddress();
        this.detailAddress = member.getAddress().getDetailAddress();
    }

}
