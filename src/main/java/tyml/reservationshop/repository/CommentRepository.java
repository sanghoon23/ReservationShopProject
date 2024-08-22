package tyml.reservationshop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.engine.resolver.JPATraversableResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tyml.reservationshop.domain.Comment;
import tyml.reservationshop.domain.Item;
import tyml.reservationshop.domain.Place;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.place.id = :placeId")
    List<Comment> findByPlaceId(Long placeId);

}
