package tyml.reservationshop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
public class UserItem {

    @Id
    @GeneratedValue
    @Column(name = "userItem_id")
    private Long id;

    private String itemName;

    private Long price;

    private String uploadImageFileName;


    public UserItem(){}

    @Builder
    public UserItem(String itemName, Long price, String uploadImageFileName) {
        this.itemName = itemName;
        this.price = price;
        this.uploadImageFileName = uploadImageFileName;
    }
}
