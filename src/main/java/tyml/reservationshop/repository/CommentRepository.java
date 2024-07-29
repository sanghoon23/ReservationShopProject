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

    public List<Comment> findCommentByPlaceId(Long placeId) {
        return em.createQuery("select c from Comment c where c.place.id = :placeId", Comment.class)
                .setParameter("placeId", placeId)
                .getResultList();
    }

}
