package tyml.reservationshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.*;
import tyml.reservationshop.domain.dto.PlaceForm;
import tyml.reservationshop.repository.CommentRepository;
import tyml.reservationshop.repository.ItemRepository;
import tyml.reservationshop.repository.PlaceRepository;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final ItemRepository itemRepository;


    @Transactional
    public Long join(Place place) {
        placeRepository.save(place);
        return place.getId();
    }

    @Transactional
    public void addItem(Long placeId, Item item) {
        Place findPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + placeId));
        findPlace.getItems().add(item);
    }

    @Transactional
    public void addAllItems(Long placeId, List<Item> items) {

        Place findPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + placeId));
        findPlace.getItems().addAll(items); //@한번의 쿼리로 한번에 추가.
    }

    @Transactional
    public void addReservation(Long placeId, Reservation reservation) {

        Place findPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + placeId));
        findPlace.getReservations().add(reservation);
    }

    @Transactional
    public void updatePlace(Long placeId, PlaceForm placeForm) {

        Place findPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + placeId));
        findPlace.updateFromPlaceForm(placeForm);
    }

    @Transactional
    public void deleteAllPlaceItemList(Long placeId) {

        List<Item> items = itemRepository.findByPlaceId(placeId);
        itemRepository.deleteAll(items);

        Place findPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + placeId));
        findPlace.getItems().clear();
    }

    @Transactional
    public void deleteItemInPlaceItemList(Long placeId, Item item) {

        Place findPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + placeId));

        findPlace.getItems().remove(item);
    }


    @Transactional
    public void deleteReservation(Long placeId, Long reservationId) {
        Place findPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + placeId));
        placeRepository.delete(findPlace);

        Reservation findReservation = findPlace.getReservations().stream()
                .filter(reservation -> reservation.getId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID : " + reservationId));

        findPlace.getReservations().remove(findReservation);
    }


    @Transactional
    public void deletePlace(Long placeId) {
        Place findPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + placeId));
        placeRepository.delete(findPlace);
    }


    /*
    @Place
************************************************************************************************************************************
     */

    public Place findOne(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + placeId));
    }

    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    public List<Place> findByPlaceName(String placeName) {
        return placeRepository.findByPlaceName(placeName);
    }

    public String findByPlaceImagePath(Long placeId) {
        return placeRepository.findByPlaceImagePath(placeId);
    }

    public List<Place> findByCategory(String category) {
        return placeRepository.findByCategory(category);
    }

    public List<Place> findByNameContaining(String searchName) {
        // 검색어 빈 문자열인 경우
        if (searchName.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return placeRepository.findByNameContaining(searchName);
    }

}
