package tyml.reservationshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tyml.reservationshop.service.ReservationService;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservation/createReservationForm")
    public String createReservationForm(Model model) {

        //model.addAttribute("reservationForm", new )

        return "/reservation/createReservationForm";
    }


}
