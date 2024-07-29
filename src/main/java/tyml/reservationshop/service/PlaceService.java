package tyml.reservationshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Comment;
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
    private final CommentRepository commentRepository;


    @Transactional
    public Long join(Place place) {
        placeRepository.save(place);
        return place.getId();
    }

    @Transactional
    public void updatePlace(Long placeId, PlaceForm placeForm) {

        Place findPlace = placeRepository.findOne(placeId);
        findPlace.UpdateFromPlaceForm(placeForm);
    }

    @Transactional
    public void deletePlace(Long placeId) {
        placeRepository.deletePlaceByPlaceId(placeId);
    }

    @Transactional
    public Comment addCommentToPlace(String content, Long userId, Long placeId) {
        Place place = placeRepository.findOne(placeId);
        if (place == null) {
            throw new RuntimeException("Place not found with id: " + placeId);
        }


        Comment comment = new Comment(content, userId, place);
        commentRepository.save(comment);
        return comment;
    }


    /*
    @Place
************************************************************************************************************************************
     */

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

    public List<Place> findByNameContaining(String searchName) {
        // 검색어 빈 문자열인 경우
        if(searchName.trim().isEmpty()){
            return Collections.emptyList();
        }
        return placeRepository.findByNameContaining(searchName);
    }

    /*
    @Comment
************************************************************************************************************************************
     */

    public List<Comment> findCommentListByPlaceId(Long placeId) {
        return commentRepository.findCommentByPlaceId(placeId);
    }

}
