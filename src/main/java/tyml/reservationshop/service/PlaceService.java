package tyml.reservationshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Comment;
import tyml.reservationshop.domain.Item;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.domain.dto.PlaceForm;
import tyml.reservationshop.repository.CommentRepository;
import tyml.reservationshop.repository.PlaceRepository;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;


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
    public void updatePlace(Long placeId, PlaceForm placeForm) {

        Place findPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + placeId));
        findPlace.updateFromPlaceForm(placeForm);
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
        if(searchName.trim().isEmpty()){
            return Collections.emptyList();
        }
        return placeRepository.findByNameContaining(searchName);
    }

}
