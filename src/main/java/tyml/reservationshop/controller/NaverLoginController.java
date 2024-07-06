package tyml.reservationshop.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.domain.dto.NaverUserInfoResponseDto;
import tyml.reservationshop.service.MemberService;
import tyml.reservationshop.service.NaverLoginService;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NaverLoginController {

    private final MemberService memberService;
    private final NaverLoginService naverLoginService;


    @GetMapping("/oauth2/callback/naver")
    public void naverLogin(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           HttpSession session, HttpServletResponse response) throws IOException {
        String accessToken = naverLoginService.getAccessTokenFromNaver(code, state);


        NaverUserInfoResponseDto userInfo = naverLoginService.getUserInfo(accessToken);

        String userEmail = userInfo.getNaverUserInfoResponse().getEmail();

        session.setAttribute("accessToken", accessToken);
        session.setAttribute("userEmail", userEmail);

        Optional<Member> member = Optional.ofNullable(memberService.findByEmail(userEmail));
        if(member.isEmpty()) // 이메일 없으면 계정 생성 페이지 전환
        {

            MemberForm form = new MemberForm();
            form.setName(userInfo.getNaverUserInfoResponse().getName());
            form.setEmail(userInfo.getNaverUserInfoResponse().getEmail());
            form.setCreateId(userInfo.getNaverUserInfoResponse().getNickname());

            session.setAttribute("memberForm", form);

            response.sendRedirect("/members/createMemberForm");
            return;
        }//@return


        log.info("Success naverLogin In!");
        response.sendRedirect("/");
    }

    @GetMapping("/oauth2/naverLogout")
    public void logoutFromNaver(HttpSession session, HttpServletResponse response) throws IOException {

        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            naverLoginService.logoutFromNaver(accessToken);
        }

        // 세션 무효화
        session.invalidate();

        //return "redirect:/";
        response.sendRedirect("/");
    }


}
