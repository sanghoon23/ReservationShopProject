package tyml.reservationshop.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.MyPageUserPasswordChangeForm;
import tyml.reservationshop.domain.dto.AuthenticationForm;
import tyml.reservationshop.domain.dto.MemberModifyForm;
import tyml.reservationshop.service.ReservationService;
import tyml.reservationshop.service.user.MemberService;

import java.io.IOException;

@Slf4j
@Controller
@Getter
@RequiredArgsConstructor
public class MyPageController {

    private final ReservationService reservationService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/myPage/myPageReservList")
    public String myPageReservList() {

        return "/myPage/myPageReservList";
    }


    /*
    CHANGE USER INFO
**************************************************************************************************************************************************
     */

    @GetMapping("/myPage/Authentication/userInfo")
    public String myPageAuthenticationUserInfo(Model model) {

        model.addAttribute("authenticationForm", new AuthenticationForm());
        return "/myPage/Authentication/myPageAuthenticationUserInfo";
    }

    @PostMapping("/myPage/Authentication/userInfo/check")
    public String myPageAuthenticationUserInfoCheck(Model model,
                                            @Valid AuthenticationForm form,
                                            BindingResult bindingResult,
                                            @AuthenticationPrincipal User user
                                            ) throws IOException {


        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "비밀번호를 입력하세요!");
            return "/myPage/Authentication/myPageAuthenticationUserInfo";
        }

        Member findMember = memberService.findByEmail(user.getUsername());
        boolean isPasswordMatch = passwordEncoder.matches(form.getPw(), findMember.getPw());
        if(!isPasswordMatch) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "/myPage/Authentication/myPageAuthenticationUserInfo";
        }

        return "redirect:/myPage/modify/userInfo";
    }


    @GetMapping("/myPage/modify/userInfo")
    public String myPageModifyUserInfo(Model model, @AuthenticationPrincipal User user) {


        Member findMember = memberService.findByEmail(user.getUsername());
        MemberModifyForm memberModifyForm = new MemberModifyForm(findMember);
        model.addAttribute("memberModifyForm", memberModifyForm);
        model.addAttribute("memberId", findMember.getId());

        return "/myPage/myPageModifyUserInfo";
    }

    @PostMapping("/myPage/modify/userInfo/submit/{memberId}")
    public String submitMyPageModifyUserInfoForm(@PathVariable("memberId") Long memberId,
                                                 @RequestParam(value = "cancel", required = false) String cancel,
                                                 @Valid MemberModifyForm memberModifyForm,
                                                 BindingResult bindingResult) {


        if ("true".equals(cancel)) {
            return "redirect:/myPage/Authentication/userInfo";
        }

        if (bindingResult.hasErrors()) {
            return "/myPage/myPageModifyUserInfo";
        }

        memberService.updateMember(memberId, memberModifyForm);

        return "/myPage/success/myPageModifySuccess";
    }


    /*
    CHANGE PASSWORD
**************************************************************************************************************************************************
     */



    @GetMapping("/myPage/Authentication/userPassword")
    public String myPageAuthenticationUserPassword(Model model) {

        model.addAttribute("authenticationForm", new AuthenticationForm());
        return "/myPage/Authentication/MyPageAuthenticationUserPassword";
    }

    @PostMapping("/myPage/Authentication/userPassword/check")
    public String myPageAuthenticationUserPasswordCheck(Model model,
                                            @Valid AuthenticationForm form,
                                            BindingResult bindingResult,
                                            @AuthenticationPrincipal User user) throws IOException {


        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "비밀번호를 입력하세요!");
            return "/myPage/Authentication/MyPageAuthenticationUserPassword";
        }

        Member findMember = memberService.findByEmail(user.getUsername());
        boolean isPasswordMatch = passwordEncoder.matches(form.getPw(), findMember.getPw());
        if(!isPasswordMatch) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "/myPage/Authentication/MyPageAuthenticationUserPassword";
        }

        return "redirect:/myPage/modify/userPassword";
    }


    @GetMapping("/myPage/modify/userPassword")
    public String myPageModifyUserPassword(Model model, @AuthenticationPrincipal User user) {


        model.addAttribute("myPageUserPasswordChangeForm", new MyPageUserPasswordChangeForm());
        Member findMember = memberService.findByEmail(user.getUsername());
        model.addAttribute("memberId", findMember.getId());


        return "/myPage/myPageModifyUserPassword";
    }

    @PostMapping("/myPage/modify/userPassword/submit/{memberId}")
    public String submitMyPageModifyUserPasswordForm(Model model,
                                                     @PathVariable("memberId") Long memberId,
                                                     @Valid MyPageUserPasswordChangeForm form,
                                                     BindingResult bindingResult) {



        if (bindingResult.hasErrors()) {
            log.info("bindingResult hassERORR!!");
            model.addAttribute("error", "비밀번호를 입력하세요!");
            return "/myPage/myPageModifyUserPassword";
        }

        //TODO : 변경할 비밀번호 와 비밀번호 일치 확인
        if (!form.getPw().equals(form.getPwCheck())) {
            model.addAttribute("error", "비밀번호 확인 불일치!");
            return "/myPage/myPageModifyUserPassword";
        }


        memberService.updatePassword(memberId, form.getPw());

        return "/myPage/success/myPageModifySuccess";
    }


}
