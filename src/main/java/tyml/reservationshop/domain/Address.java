package tyml.reservationshop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String postcodes;
    private String address;
    private String detailAddress;

    protected Address() {
    }

    public Address(String postcodes, String address, String detailAddress) {
        this.postcodes = postcodes;
        this.address = address;
        this.detailAddress = detailAddress;
    }
}
