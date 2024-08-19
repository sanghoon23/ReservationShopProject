package tyml.reservationshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tyml.reservationshop.domain.dto.ReservationForm;
import tyml.reservationshop.service.ReservationService;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservation/createReservationForm")
    public String createReservationForm(Model model) {

        model.addAttribute("reservationForm", new ReservationForm());
        return "/reservation/createReservationForm";
    }

    @PostMapping("/reservation/create/date")
    public String createReservationDate(Model model) {



        return "/reservation/processReservationForm";
    }

    @PostMapping("/reservation/create/userinfo")
    public String createReservationUserInfo(Model model) {


        //TODO : reserv save

        return "home";
    }


}
