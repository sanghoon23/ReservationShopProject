package tyml.reservationshop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String zipcodes;
    private String street;

    protected Address() {
    }

    public Address(String zipcodes, String street) {

        this.zipcodes = zipcodes;
        this.street = street;
    }
}
