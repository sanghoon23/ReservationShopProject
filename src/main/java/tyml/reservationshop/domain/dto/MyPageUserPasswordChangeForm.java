package tyml.reservationshop.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MyPageUserPasswordChangeForm {

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String pw;

    @NotEmpty(message = "비밀번호 확인을 입력하세요")
    private String pwCheck;

    public  MyPageUserPasswordChangeForm(){}
}
