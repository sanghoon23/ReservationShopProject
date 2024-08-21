package tyml.reservationshop.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
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

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<Item>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<Reservation>();


    public  Place() {
        address = new Address();
    }
    public Place(PlaceForm form) {
        this.placeName = form.getPlaceName();
        this.address = new Address(form.getPostcodes(), form.getMainAddress(), form.getDetailAddress());
        this.description = form.getDescription();
        this.uploadImageFileName = form.getUploadImageFileName();
        this.category = form.getCategory();
    }

    public void updateFromPlaceForm(PlaceForm form) {
        this.placeName = form.getPlaceName();
        this.address = new Address(form.getPostcodes(), form.getMainAddress(), form.getDetailAddress());
        this.description = form.getDescription();
        this.uploadImageFileName = form.getUploadImageFileName();
        this.category = form.getCategory();
    }

}
