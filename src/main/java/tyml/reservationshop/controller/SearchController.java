package tyml.reservationshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tyml.reservationshop.service.PlaceService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchController {

    final private PlaceService placeService;

    @PostMapping("/search/place")
    public String handleSearch(@RequestParam("search") String searchName, Model model) {

        model.addAttribute("searchName", searchName);
        model.addAttribute("places", placeService.findByNameContaining(searchName));

        return "/place/searchPlaceList";
    }

}
