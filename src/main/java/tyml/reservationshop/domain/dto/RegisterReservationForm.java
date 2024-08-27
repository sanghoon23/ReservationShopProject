package tyml.reservationshop.domain.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RegisterReservationForm {

    @NotEmpty(message = "이름을 입력하세요.")
    private String name;

    @NotEmpty(message = "휴대전화를 입력하세요.")
    private String phoneNumber;

    @NotEmpty(message = "이메일을 입력하세요.")
    @Email
    private String email;

    private String postcodes = " ";

    private String mainAddress = " ";

    private String detailAddress = " ";

    @NotEmpty(message = "")
    private String reservDate;

    @NotEmpty(message = "")
    private String reservTime;

    private List<Long> selectedItemIds = new ArrayList<Long>();

    @Lob
    private String requiredContent; // 요구사항
}
