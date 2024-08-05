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
    private String content;
    private LocalDateTime createdAt;

    // Factory method to create CommentForm from Comment
    public static CommentDto from(Comment comment) {
        CommentDto form = new CommentDto();
        form.setContent(comment.getContent());
        form.setCreatedAt(comment.getCreatedAt());
        return form;
    }
}
