package tyml.reservationshop.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class PlaceForm {

    @NotEmpty(message = "")
    private String placeName;

    //그림?

    @NotEmpty(message = "")
    private String postcodes;

    @NotEmpty(message = "")
    private String address;

    @NotEmpty(message = "")
    private String detailAddress;

    //예약 장소 설명
    @NotEmpty(message = "")
    private String description;

    private String imagePath;

    @NotEmpty(message = "")
    private String category;

}
