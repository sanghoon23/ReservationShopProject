package tyml.reservationshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.service.PlaceService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryAtPlaceController {

    private final PlaceService placeService;

    @GetMapping("/category/{categoryName}")
    public String categoryList(@PathVariable("categoryName") String categoryName, Model model) {

        List<Place> placeList = placeService.findByCategory(categoryName);

        model.addAttribute("places", placeList);
        model.addAttribute("categoryName", categoryName);

        return "/place/categoryPlaceList";
    }
}
