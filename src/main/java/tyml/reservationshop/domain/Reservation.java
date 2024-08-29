package tyml.reservationshop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    // 단방향 OneToMany 관계 설정
    @OneToMany
    @JoinColumn(name = "reservation_id")  // 외래키를 정의합니다.
    private List<Item> itemList = new ArrayList<>();

    private String reservDate;

    private String reservTime;

    @Lob
    private String requiredContent; // 요구사항

    public Reservation(){}

    @Builder
    public Reservation(Member member, Place place, String reservDate, String reservTime, String requiredContent) {
        this.member = member;
        this.place = place;
        this.reservDate = reservDate;
        this.reservTime = reservTime;
        this.requiredContent = requiredContent;
    }

}
