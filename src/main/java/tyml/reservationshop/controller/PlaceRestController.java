package tyml.reservationshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.service.PlaceService;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaceRestController {

    private final PlaceService placeService;

    @GetMapping("/place/detail/{placeId}")
    public ResponseEntity<Place> getPlaceDetail(@PathVariable("placeId") Long placeId) {
        Place place = placeService.findOne(placeId);
        if (place != null) {
            return ResponseEntity.ok(place);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
