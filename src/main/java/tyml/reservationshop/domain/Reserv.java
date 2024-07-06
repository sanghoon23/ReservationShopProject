package tyml.reservationshop.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Reserv {

    @Id @GeneratedValue
    @Column(name = "Reserv_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
