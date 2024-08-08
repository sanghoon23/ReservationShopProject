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
    private Long userId;
    private String userName;

    private Long commentId;
    private String content;

    private LocalDateTime createDateTime;

    private Long currentUserId = 0L;

    // Factory method to create CommentForm from Comment
    public static CommentDto from(Comment comment, Long currentUserId) {
        CommentDto form = new CommentDto();
        form.setUserId(comment.getUserId());
        form.setUserName(comment.getUserName());
        form.setCommentId(comment.getId());
        form.setContent(comment.getContent());
        form.setCreateDateTime(comment.getCreatedAt());
        form.setCurrentUserId(currentUserId);
        return form;
    }
}
