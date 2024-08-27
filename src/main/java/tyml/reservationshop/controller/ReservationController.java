package tyml.reservationshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import tyml.reservationshop.domain.Item;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.domain.dto.AuthenticationForm;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.domain.dto.RegisterReservationForm;
import tyml.reservationshop.domain.dto.ReservationForm;
import tyml.reservationshop.service.ItemService;
import tyml.reservationshop.service.PlaceService;
import tyml.reservationshop.service.ReservationService;
import tyml.reservationshop.service.user.MemberService;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final PlaceService placeService;
    private final MemberService memberService;


    @GetMapping("/reservation/createReservationForm/{placeId}")
    public String createReservationForm(Model model,
                                        @PathVariable("placeId") Long placeId) {

        model.addAttribute("reservationForm", new ReservationForm());
        Place findPlace = placeService.findOne(placeId);
        model.addAttribute("items", findPlace.getItems());

        return "/reservation/createReservationForm";
    }

    @PostMapping("/reservation/createReservationForm/date/{placeId}")
    public String createReservationSubmitForm(Model model,
                                              @PathVariable("placeId") Long placeId,
                                              @Valid ReservationForm reservationForm,
                                              BindingResult bindingResult,
                                              @AuthenticationPrincipal User user) {

        if (bindingResult.hasErrors()) {
            log.info("createReservationSumitForm bindingResult ERROR!!");

            Place findPlace = placeService.findOne(placeId);
            model.addAttribute("items", findPlace.getItems());
            model.addAttribute("error", "날짜와 시간 또는 상품을 확인해주세요.");
            return "reservation/createReservationForm";
        }

        model.addAttribute("reservationForm", reservationForm);
        model.addAttribute("place", placeService.findOne(placeId));

        Member findMember = memberService.findByEmail(user.getUsername());
        model.addAttribute("member", findMember);
        model.addAttribute("memberForm", new MemberForm(findMember));

        model.addAttribute("registerReservationForm", new RegisterReservationForm());

        return "/reservation/processReservationForm";
    }

    @PostMapping("/reservation/register/{placeId}/{memberId}")
    public String registerReservation(Model model,
                                      @Valid RegisterReservationForm form,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "약관을 동의해주세요.");
            return "reservation/createReservationForm";
        }


        return "/reservation/success/registerReservationSuccess";
    }

//
//    @GetMapping("/reservation/processReservationForm/{placeId}")
//    public String processReservationForm(Model model) {
//
//        return "/reservation/processReservationForm";
//    }



//    @PostMapping("/reservation/create/userinfo")
//    public String createReservationUserInfo(Model model) {
//
//
//        //TODO : reserv save
//
//        return "home";
//    }


}
