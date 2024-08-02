package tyml.reservationshop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
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
        //return em.find(Place.class, placeId);
        return em.createQuery("select p from Place p where p.id = :placeId", Place.class)
                .setParameter("placeId", placeId)
                .getSingleResult();
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

    public List<Place> findByNameContaining(String searchQuery) {
        String query = "SELECT p FROM Place p WHERE p.placeName LIKE CONCAT('%', :searchQuery, '%')";
        return em.createQuery(query, Place.class)
                .setParameter("searchQuery", searchQuery)
                .getResultList();
    }

}
