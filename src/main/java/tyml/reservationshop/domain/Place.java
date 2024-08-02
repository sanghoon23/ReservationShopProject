package tyml.reservationshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import tyml.reservationshop.domain.dto.PlaceForm;

import java.util.ArrayList;
import java.util.List;

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

    private String uploadImageFileName;

    private String category;

    @OneToMany(mappedBy = "place")
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    public  Place() {
        address = new Address();
    }
    public Place(PlaceForm form) {
        this.placeName = form.getPlaceName();
        this.address = new Address(form.getPostcodes(), form.getAddress(), form.getDetailAddress());
        this.description = form.getDescription();
        this.uploadImageFileName = form.getUploadImageFileName();
        this.category = form.getCategory();
    }

    public void UpdateFromPlaceForm(PlaceForm form) {
        this.placeName = form.getPlaceName();
        this.address = new Address(form.getPostcodes(), form.getAddress(), form.getDetailAddress());
        this.description = form.getDescription();
        this.uploadImageFileName = form.getUploadImageFileName();
        this.category = form.getCategory();
    }

}
