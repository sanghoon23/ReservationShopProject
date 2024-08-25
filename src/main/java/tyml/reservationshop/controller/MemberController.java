package tyml.reservationshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tyml.reservationshop.domain.dto.EmailSenderDto;
import tyml.reservationshop.domain.dto.LoginForm;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.domain.dto.MemberModifyForm;
import tyml.reservationshop.service.user.MemberService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@Getter
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    /*
**********************************************************************************************************
     */

    @GetMapping("/members/loginMemberPage")
    public String loginMemberPage(Model model, HttpSession session) {

        model.addAttribute("loginForm", new LoginForm());
        return "/members/loginMemberPage";
    }


    @GetMapping("/members/createMemberForm")
    public String createMemberForm(Model model, HttpSession session) {


        MemberForm form = (MemberForm)session.getAttribute("memberForm");
        model.addAttribute(
                "memberForm"
                , (form == null)? new MemberForm() : form
        );

        model.addAttribute(
                "emailSenderDto"
                , new EmailSenderDto()
        );

        return "/members/createMemberForm";
    }

    @PostMapping("/members/createMemberForm")
    public String createMemberForm(@Valid MemberForm memberForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/members/createMemberForm";
        }

        if (!memberForm.getPw().equals(memberForm.getPwCheck())) {
            bindingResult.rejectValue("pw", "passwardInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "/members/createMemberForm";
        }

        memberForm.setPw(passwordEncoder.encode(memberForm.getPw()));
        Member member = new Member(memberForm);

        try {
            memberService.join(member);
        } catch (DataIntegrityViolationException e) { //@Email 중복 Exception
            e.printStackTrace();
            bindingResult.rejectValue("email", "signupFailed", "이미 등록된 사용자입니다.");
            return "/members/createMemberForm";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "/members/createMemberForm";
        }

        return "redirect:/";
    }

    @GetMapping("/members/memberList")
    public String memberList(Model model) {
        List<Member> memberList = memberService.findAll();
        model.addAttribute("members", memberList);
        return "/members/memberList";
    }

    @GetMapping("/members/modifyMemberForm/{memberId}")
    public String modifyMemberForm(@PathVariable("memberId") Long memberId, HttpSession session,  Model model) {

        model.addAttribute("memberId", memberId);

        Member member = memberService.findOne(memberId);
        model.addAttribute("memberModifyForm", new MemberModifyForm(member));

        return "/members/modifyMemberForm";
    }

    @PostMapping("/members/modifyMemberForm/modify/{memberId}")
    public String submitModifyMemberForm(@PathVariable("memberId") Long memberId,
                                         @RequestParam(value = "cancel", required = false) String cancel,
                                         @Valid MemberModifyForm memberModifyForm,
                                         BindingResult bindingResult) {


        if ("true".equals(cancel)) {
            return "redirect:/members/memberList";
        }

        if (bindingResult.hasErrors()) {
            return "/members/modifyMemberForm";
        }

        memberService.updateMember(memberId, memberModifyForm);

        return "redirect:/members/memberList";
    }


    @GetMapping("/members/deleteMember/{memberId}")
    public String deleteMember(@PathVariable("memberId") Long memberId) {

        memberService.deleteMember(memberId);
        return "redirect:/members/memberList";
    }

    //*************************************************************************





}
