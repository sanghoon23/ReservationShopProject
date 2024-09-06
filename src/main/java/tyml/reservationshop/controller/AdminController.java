package tyml.reservationshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.service.PlaceService;
import tyml.reservationshop.service.ReservationService;
import tyml.reservationshop.service.user.MemberService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;
    private final PlaceService placeService;
    private final ReservationService reservationService;

    @GetMapping("/admin")
    public String adminHome() {
        return "/admin/adminPage";
    }

    @GetMapping("/admin/memberList")
    public String memberList(Model model) {
        List<Member> memberList = memberService.findAll();
        model.addAttribute("members", memberList);

        return "/admin/adminMemberList";
    }

    @GetMapping("/admin/placeList")
    public String placeList(Model model) {
        List<Place> placeList = placeService.findAll();
        model.addAttribute("places", placeList);

        return "/admin/adminPlaceList";
    }

    @GetMapping("/admin/reservationList")
    public String myPageReservList(Model model) {

        model.addAttribute("reservations", reservationService.findAll());
        return "/admin/adminReservationList";
    }

}
