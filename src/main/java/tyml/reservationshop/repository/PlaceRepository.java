package tyml.reservationshop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tyml.reservationshop.domain.Item;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.Place;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByPlaceName(String placeName);

    @Query("SELECT p.uploadImageFileName FROM Place p WHERE p.id = :placeId")
    String findByPlaceImagePath(Long placeId);

    List<Place> findByCategory(String category);

    @Query("SELECT p FROM Place p WHERE p.placeName LIKE %:searchQuery%")
    List<Place> findByNameContaining(String searchQuery);
}
