package tyml.reservationshop.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailAuthDto {

    @NotEmpty(message = "")
    private String email;

    @NotEmpty(message = "")
    private String authNumber;

}
