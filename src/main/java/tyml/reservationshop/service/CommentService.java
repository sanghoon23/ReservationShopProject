package tyml.reservationshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Comment;
import tyml.reservationshop.repository.CommentRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Long join(Comment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional
    public void updateComment(Long commentId, String content) {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
        findComment.setContent(content);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
        commentRepository.delete(findComment);
    }

    /*
    @Comment
************************************************************************************************************************************
     */

    public Comment findOne(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
    }

    public List<Comment> findByPlaceId(Long placeId) {
        return commentRepository.findByPlaceId(placeId);
    }

}
