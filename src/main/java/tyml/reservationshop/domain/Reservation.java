package tyml.reservationshop.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Reservation {

    @Id @GeneratedValue
    @Column(name = "reserv_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;


}
