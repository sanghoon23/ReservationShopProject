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
    private String zipcodes;

    @NotEmpty(message = "")
    private String street;

    //예약 장소 설명
    @NotEmpty(message = "")
    private String description;

    private String imagePath;
}
