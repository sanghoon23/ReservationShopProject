package tyml.reservationshop.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tyml.reservationshop.domain.Item;
import tyml.reservationshop.domain.Place;

import java.util.List;

@Getter
@Setter
public class PlaceForm {

    @NotEmpty(message = "장소 이름을 입력하세요.")
    private String placeName;

    @NotEmpty(message = "우편 번호를 입력하세요.")
    private String postcodes;

    @NotEmpty(message = "주소를 입력하세요.")
    private String mainAddress;

    private String detailAddress;

    //예약 장소 설명
    @NotEmpty(message = "설명란을 입력하세요.")
    private String description;

    private String uploadImageFileName;

    @NotEmpty(message = "카테고리를 수정하세요.")
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
