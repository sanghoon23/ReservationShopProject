package tyml.reservationshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tyml.reservationshop.domain.Comment;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.service.PlaceService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaceRestController {

    private final PlaceService placeService;

    @GetMapping("/place/map/detail/{placeId}")
    public ResponseEntity<Place> getPlaceDetail(@PathVariable("placeId") Long placeId) {
        Place place = placeService.findOne(placeId);

        if (place != null) {
            return ResponseEntity.ok(place);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/place/commentList/{placeId}")
    public ResponseEntity<List<Comment>> getPlaceCommentList(@PathVariable("placeId") Long placeId) {

        List<Comment> commentList = placeService.findCommentListByPlaceId(placeId);
        return ResponseEntity.ok(commentList);
    }

    @PostMapping("/place/commentList/{placeId}")
    public ResponseEntity<Comment> addCommentToPlace(@PathVariable Long placeId,
                                                     @RequestParam String content) {
        //현재 로그인된 Member 의 userId 구하기
        Long userId = 0L;

        Comment comment = placeService.addCommentToPlace(content, userId, placeId);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }

}
