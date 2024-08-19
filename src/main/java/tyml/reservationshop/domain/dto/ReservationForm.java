package tyml.reservationshop.domain.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ReservationForm {

    @NotEmpty(message = "날짜를 선택해주세요.")
    private String reservDate;

    @NotEmpty(message = "시간을 선택해주세요.")
    private String reservTime;

    @NotEmpty(message = "이름을 입력하세요.")
    private String username;

    @NotEmpty(message = "메일을 입력하세요.")
    private String email;

    @NotEmpty(message = "전화번호를 입력하세요.")
    private String phoneNumber;

    @Lob
    private String requiredContent; // 요구사항

}
