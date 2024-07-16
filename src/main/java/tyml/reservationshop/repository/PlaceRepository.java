package tyml.reservationshop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.Place;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceRepository {

    private final EntityManager em;

    public void save(Place place) {
        em.persist(place);
    }

    public Place findOne(Long placeId) {
        return em.find(Place.class, placeId);
    }

    public List<Place> findAll() {
        return em.createQuery("select p from Place p", Place.class)
                .getResultList();
    }

    public List<Place> findByPlaceName(String placeName) {
        return em.createQuery("select p from Place p where p.placeName = :placeName", Place.class)
                .setParameter("placeName", placeName)
                .getResultList();
    }

    public String findByPlaceImagePath(Long placeId) {
        return em.createQuery("select p.uploadImageFileName from Place p where p.id = :placeId", String.class)
                .setParameter("placeId", placeId)
                .getSingleResult();
    }

    public List<Place> findByCategory(String category) {
        return em.createQuery("select p from Place p where p.category = :category", Place.class)
                .setParameter("category", category)
                .getResultList();
    }

    public void deletePlaceByPlaceId(Long placeId) {

        em.createQuery("DELETE FROM Place p WHERE p.id = :placeId")
                .setParameter("placeId", placeId)
                .executeUpdate();

    }

}
