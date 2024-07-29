package tyml.reservationshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;

import java.time.LocalDateTime;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue
    private Long id;


    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Lob
    private String content;

    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;



    public Comment(){}

    public Comment(String content, Long userId, Place place) {
        this.content = content;
        this.userId = userId;
        this.place = place;
        this.createdAt = LocalDateTime.now();
    }


}
