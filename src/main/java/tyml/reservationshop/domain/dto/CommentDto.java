package tyml.reservationshop.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tyml.reservationshop.domain.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private String userName;
    private String content;
    private LocalDateTime createDateTime;

    // Factory method to create CommentForm from Comment
    public static CommentDto from(Comment comment) {
        CommentDto form = new CommentDto();
        form.setUserName(comment.getUserName());
        form.setContent(comment.getContent());
        form.setCreateDateTime(comment.getCreatedAt());
        return form;
    }
}
