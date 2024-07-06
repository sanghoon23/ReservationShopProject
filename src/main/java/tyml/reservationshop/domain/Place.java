package tyml.reservationshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import tyml.reservationshop.domain.dto.PlaceForm;

@Getter
@Entity
public class Place {

    @Id @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    private String placeName;

    @Embedded
    private Address address;

    private String description;

    private String imagePath;

    protected  Place() {}
    public Place(PlaceForm form) {
        this.placeName = form.getPlaceName();
        this.address = new Address(form.getZipcodes(), form.getStreet());
        this.description = form.getDescription();
        this.imagePath = form.getImagePath();
    }

}
