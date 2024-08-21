package tyml.reservationshop.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    private Long price;

    private String uploadImageFileName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Builder
    public Item(String itemName, Long price, String uploadImageFileName, Place place) {
        this.itemName = itemName;
        this.price = price;
        this.uploadImageFileName = uploadImageFileName;
        this.place = place;
    }

}
