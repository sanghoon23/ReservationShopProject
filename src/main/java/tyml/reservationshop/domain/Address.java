package tyml.reservationshop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
public class Address {
    private String Postcodes = " ";
    private String MainAddress = " ";
    private String DetailAddress = " ";

    protected Address() {
    }

    public Address(String postcodes, String mainAddress, String detailAddress) {
        this.Postcodes = postcodes;
        this.MainAddress = mainAddress;
        this.DetailAddress = detailAddress;
    }
}
