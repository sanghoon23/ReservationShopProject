package tyml.reservationshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tyml.reservationshop.service.PlaceService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class    HomeController {

    private final PlaceService placeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {

        model.addAttribute("places", placeService.findAll());

        return "home";
    }


    @GetMapping("/successPage")
    public String successPage() {
        return "/myPage/myPageModifyUserInfoSuccess";
    }

}
