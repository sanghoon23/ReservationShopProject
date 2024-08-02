package tyml.reservationshop.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginForm {

    @NotEmpty(message = "아이디 필수에요.")
    private String email;

    @NotEmpty(message = "비밀번호 필수예요.")
    private String pw;
}
