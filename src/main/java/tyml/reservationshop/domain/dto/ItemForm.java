package tyml.reservationshop.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemForm {

    @NotEmpty(message = "")
    private Long id;

    @NotEmpty(message = "")
    private String itemName;

    @NotEmpty(message = "")
    private Long price;

    @NotEmpty(message = "")
    private String uploadImageFileName;

}
