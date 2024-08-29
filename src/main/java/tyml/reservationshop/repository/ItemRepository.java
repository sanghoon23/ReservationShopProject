package tyml.reservationshop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tyml.reservationshop.domain.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.place.id = :placeId")
    List<Item> findByPlaceId(Long placeId);

    @Query("SELECT i FROM Item i WHERE i.id IN :Ids")
    List<Item> findItemsByIds(List<Long> Ids);


    List<Item> findByIdIn(List<Long> itemIds);
}
