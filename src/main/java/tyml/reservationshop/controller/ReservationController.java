package tyml.reservationshop.controller;

import jakarta.servlet.http.HttpSession;
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
import tyml.reservationshop.domain.*;
import tyml.reservationshop.domain.dto.AuthenticationForm;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.domain.dto.RegisterReservationForm;
import tyml.reservationshop.domain.dto.ReservationForm;
import tyml.reservationshop.service.ItemService;
import tyml.reservationshop.service.PlaceService;
import tyml.reservationshop.service.ReservationService;
import tyml.reservationshop.service.user.MemberService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final PlaceService placeService;
    private final MemberService memberService;
    private final ItemService itemService;


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
                                              @AuthenticationPrincipal User user,
                                              HttpSession session) {

        if (bindingResult.hasErrors()) {
            Place findPlace = placeService.findOne(placeId);
            model.addAttribute("items", findPlace.getItems());
            model.addAttribute("error", "날짜와 시간 또는 상품을 확인해주세요.");
            return "reservation/createReservationForm";
        }

        //받아온 item id, itemList 에 객체 넣어주기
        List<Item> findItems = itemService.findItemsByIds(reservationForm.getSelectedItemIds());
        double totalPrice = findItems.stream().mapToDouble(Item::getPrice).sum();
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("items", findItems);
        session.setAttribute("items", findItems); //@session 등록

        model.addAttribute("reservationForm", reservationForm);
        session.setAttribute("reservationForm", reservationForm); //@session 등록
        model.addAttribute("place", placeService.findOne(placeId));

        Member findMember = memberService.findByEmail(user.getUsername());
        model.addAttribute("member", findMember);
        session.setAttribute("member", findMember); //@session 등록

        model.addAttribute("memberForm", new MemberForm(findMember));

        model.addAttribute("registerReservationForm", new RegisterReservationForm());


        return "/reservation/processReservationForm";
    }

    @PostMapping("/reservation/register/{placeId}/{memberId}")
    public String registerReservation(Model model,
                                      @PathVariable("placeId") Long placeId,
                                      @PathVariable("memberId") Long memberId,
                                      @Valid RegisterReservationForm registerReservationForm,
                                      BindingResult bindingResult,
                                      HttpSession session,
                                      @AuthenticationPrincipal User user) {

        List<Item> items;
        Member member;
        ReservationForm reservationForm;

        items = getSessionAttribute(session, "items", List.class);
        member = getSessionAttribute(session, "member", Member.class);
        reservationForm = getSessionAttribute(session, "reservationForm", ReservationForm.class);

        if (items == null) {
            items = new ArrayList<>();
        }
        if (member == null) {
            member = new Member();
        }
        if (reservationForm == null) {
            reservationForm = new ReservationForm();
        }

        //@ User 가 맞는지 다시 확인
        //******************************************************************************************************
        if(user == null) {
            return "/reservation/success/registerReservationUserFail";
        }
        Member currentMember = memberService.findByEmail(user.getUsername());
        if(!currentMember.getId().equals(member.getId())){
            return "/reservation/success/registerReservationUserFail";
        }


        //@ 예약 저장 및 멤버, 장소에 등록
        {
            Reservation reservation = Reservation.builder()
                    .member(memberService.findOne(memberId))
                    .place(placeService.findOne(placeId))
                    .reservDate(reservationForm.getReservDate())
                    .reservTime(reservationForm.getReservTime())
                    .requiredContent(registerReservationForm.getRequestContent())
                    .build();

            for (Item item : items) {
                Item findItem = itemService.findOne(item.getId());

                UserItem userItem = UserItem.builder()
                        .itemName(findItem.getItemName())
                        .price(findItem.getPrice())
                        .uploadImageFileName(findItem.getUploadImageFileName())
                        .build();

                reservation.getUserItemList().add(userItem);
            }

            reservationService.join(reservation);
        }


        return "/reservation/success/registerReservationSuccess";
    }


    @GetMapping("/reservation/delete/{reservationId}")
    public String deleteReservation(@PathVariable("reservationId") Long reservationId) {

        Reservation findReservation = reservationService.findOne(reservationId);
        Member findMember = memberService.findOne(findReservation.getMember().getId());
        Place findPlace = placeService.findOne(findReservation.getPlace().getId());

        memberService.deleteReservation(findMember.getId(), findReservation.getId());
//        placeService.deleteReservation(findPlace.getId(), findReservation.getId());
//        reservationService.deleteReservation(reservationId);

        log.info("reservation Delete Success!!!");

        return "/reservation/success/registerReservationSuccess";
    }



    /*
    Custom Method
**********************************************************************************************************************************************88
     */


    // 캐스팅 헬퍼 메소드
    private <T> T getSessionAttribute(HttpSession session, String attributeName, Class<T> clazz) {
        Object attribute = session.getAttribute(attributeName);
        try {
            return clazz.cast(attribute);
        } catch (ClassCastException e) {
            System.err.println("Session attribute '" + attributeName + "' is not of type " + clazz.getSimpleName() + ".");
            return null;
        }
    }


}
