package tyml.reservationshop.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationForm {

    @NotEmpty(message = "")
    private String pw;

    public AuthenticationForm(){}

}
