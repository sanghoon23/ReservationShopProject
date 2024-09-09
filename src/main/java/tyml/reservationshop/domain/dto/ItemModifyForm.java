package tyml.reservationshop.domain.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemModifyForm {

    private String itemName;

    private Long price;

    private String uploadImageFileName;

}
