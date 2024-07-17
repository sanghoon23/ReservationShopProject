package tyml.reservationshop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import tyml.reservationshop.domain.dto.EmailSenderDto;
import tyml.reservationshop.domain.dto.LoginForm;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.domain.dto.PlaceForm;
import tyml.reservationshop.service.MemberService;

import java.net.URLEncoder;
import java.util.UUID;

@Slf4j
@Controller
@Getter
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /*
        Kakao
    */
    @Value("${kakao.client_id}")
    private String kakao_client_id;

    @Value("${kakao.redirect_uri}")
    private String kakao_redirect_uri;
    /*
    *****************************************************
     */


    /*
        NAVER
     */
    @Value("${naver.client_id}")
    private String naver_client_id;

    @Value("${naver.redirect_uri}")
    private String naver_redirect_uri;

    @Value("${naver.client_secret}")
    private String naver_client_secret;

    /*
    *****************************************************
     */

    /*
        GOOGLE
     */

    @Value("${google.client_id}")
    private String google_client_id;

    @Value("${google.client_secret}")
    private String google_client_secret;

    @Value("${google.redirect_uri}")
    private String google_redirect_uri;

    @Value("${google.scope}")
    private String google_scope;


    /*
     *****************************************************
     */

    @GetMapping("/members/loginMemberPage")
    public String loginMemberPage(Model model, HttpSession session) {

        //@NAVER INFO INSERT
        {
            String state = UUID.randomUUID().toString();
            session.setAttribute("state", state);

            String naverLocation = "https://nid.naver.com/oauth2.0/authorize?"
                    + "response_type=code"
                    + "&client_id=" + naver_client_id
                    + "&state=" + state
                    + "&redirect_uri=" + naver_redirect_uri;
            model.addAttribute("naverLocation", naverLocation);
        }

        //@KAKAO INFO INSERT
        {
            String kakaoLocation = "https://kauth.kakao.com/oauth/authorize?"
                    + "response_type=code"
                    + "&client_id=" + kakao_client_id
                    + "&redirect_uri=" + kakao_redirect_uri;
            model.addAttribute("kakaoLocation", kakaoLocation);
        }

        //@GOOGLE INFO INSERT
        {
            String googleLocation = "https://accounts.google.com/o/oauth2/v2/auth?"
                    + "client_id=" + google_client_id
                    + "&redirect_uri=" + google_redirect_uri
                    + "&response_type=code"
                    + "&scope=" + google_scope;
            model.addAttribute("googleLocation", googleLocation);
        }


        model.addAttribute("loginForm", new LoginForm());

        return "/members/loginMemberPage";
    }

    @PostMapping("/members/login")
    public String login(@Valid LoginForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "members/loginMemberPage";
        }

        return "redirect:/";
    }

    @GetMapping("/members/createMemberPage")
    public String memberMemberPage(Model model) {

        String kakaoLocation = "https://kauth.kakao.com/oauth/authorize?" + "response_type=code&" + "client_id="+ kakao_client_id +"&redirect_uri="+ kakao_redirect_uri;
        model.addAttribute("kakaoLocation", kakaoLocation);
        return "members/createMemberPage";
    }

    @GetMapping("/members/createMemberForm")
    public String createMemberForm(Model model, HttpSession session) {


        MemberForm form = (MemberForm)session.getAttribute("memberForm");
        model.addAttribute(
                "memberForm"
                , (form == null)? new MemberForm() : form
        );

        EmailSenderDto emailDto = (EmailSenderDto)session.getAttribute("emailSenderDto");
        model.addAttribute(
                "emailSenderDto"
                , new EmailSenderDto()
        );

        return "members/createMemberForm";
    }

    @PostMapping("/members/createMemberForm")
    public String createMemberForm(@Valid MemberForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = new Member(form);
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members/memberList")
    public String memberList(Model model) {
        model.addAttribute("members", memberService.findAll());
        return "members/memberList";
    }

    @GetMapping("/members/modifyMemberForm/{memberId}")
    public String modifyMemberForm(@PathVariable("memberId") Long memberId, HttpSession session,  Model model) {

        model.addAttribute("memberId", memberId);

        Member member = memberService.findOne(memberId);
        model.addAttribute("memberForm", new MemberForm(member));

        return "/members/modifyMemberForm";
    }

    @PostMapping("/members/modifyMemberForm/{memberId}")
    public String modifyMemberForm(@PathVariable("memberId") Long memberId,
                                   @RequestParam(value = "cancel", required = false) String cancel,
                                   @Valid MemberForm memberForm,
                                   BindingResult bindingResult) {

        if ("true".equals(cancel)) {
            return "redirect:/members/memberList";
        }

        if (bindingResult.hasErrors()) {
            return "/members/modifyMemberForm";
        }

        memberService.updateMember(memberId, memberForm);

        return "redirect:/members/memberList";
    }


    @GetMapping("/members/deleteMember/{memberId}")
    public String deleteMember(@PathVariable("memberId") Long memberId) {

        memberService.deleteMember(memberId);
        return "redirect:/members/memberList";
    }



    //*************************************************************************





}
