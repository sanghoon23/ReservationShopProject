package tyml.reservationshop.domain.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import tyml.reservationshop.domain.Item;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReservationForm {

    @NotEmpty(message = "")
    private String reservDate;

    @NotEmpty(message = "")
    private String reservTime;

    private List<Long> selectedItemIds = new ArrayList<Long>();

    private List<Item> selectedItems = new ArrayList<Item>();

}
