package tyml.reservationshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "COMMENTS")
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private Long userId;

    private String userName;

    @Lob
    @Setter
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    protected Comment(){}

    public Comment(String content, Long userId, String userName, Place place) {
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.place = place;
        this.createdAt = LocalDateTime.now().withSecond(0).withNano(0);
    }


}
