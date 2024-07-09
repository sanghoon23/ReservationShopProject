package tyml.reservationshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.repository.PlaceRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    //장소 등록
    @Transactional
    public Long join(Place place) {
        placeRepository.save(place);
        return place.getId();
    }

    public Place findOne(Long placeId) {
        return placeRepository.findOne(placeId);
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


}
