package tyml.reservationshop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tyml.reservationshop.domain.Comment;
import tyml.reservationshop.domain.Place;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment findOne(Long commentId) {
        return em.find(Comment.class, commentId);
    }

    public List<Comment> findCommentByPlaceId(Long placeId) {
        return em.createQuery("select c from Comment c where c.place.id = :placeId", Comment.class)
                .setParameter("placeId", placeId)
                .getResultList();
    }

    public void deleteCommentByCommentId(Long commentId) {

        em.createQuery("DELETE FROM Comment c WHERE c.id = :commentId")
                .setParameter("commentId", commentId)
                .executeUpdate();

    }

}
