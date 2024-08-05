package tyml.reservationshop.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tyml.reservationshop.domain.Place;

@Getter
@Setter
public class PlaceForm {

    @NotEmpty(message = "")
    private String placeName;

    @NotEmpty(message = "")
    private String postcodes;

    @NotEmpty(message = "")
    private String mainAddress;

    @NotEmpty(message = "")
    private String detailAddress;

    //예약 장소 설명
    @NotEmpty(message = "")
    private String description;

    private String uploadImageFileName;

    @NotEmpty(message = "")
    private String category;

    public PlaceForm() {}

    public PlaceForm(Place place) {
        this.placeName = place.getPlaceName();
        this.postcodes = place.getAddress().getPostcodes();
        this.mainAddress = place.getAddress().getMainAddress();
        this.detailAddress = place.getAddress().getDetailAddress();
        this.description = place.getDescription();
        this.uploadImageFileName = place.getUploadImageFileName();
        this.category = place.getCategory();
    }

}
