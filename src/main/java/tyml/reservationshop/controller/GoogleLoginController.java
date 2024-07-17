package tyml.reservationshop.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.GoogleUserInfoResponseDto;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.service.GoogleUserService;
import tyml.reservationshop.service.MemberService;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GoogleLoginController {

    private final MemberService memberService;
    private final GoogleUserService googleUserService;

    @GetMapping("/oauth2/callback/google")
    public void googleLogin(@RequestParam("code") String code, HttpSession session, HttpServletResponse response) throws IOException {
        String accessToken = googleUserService.getAccessTokenFromGoogle(code);

        GoogleUserInfoResponseDto userInfo = googleUserService.getUserInfo(accessToken);

        String userEmail = userInfo.getEmail();

        session.setAttribute("accessToken", accessToken);
        session.setAttribute("userEmail", userEmail);

        Optional<Member> member = Optional.ofNullable(memberService.findByEmail(userEmail));
        if(member.isEmpty()) // 이메일 없으면 계정 생성 페이지 전환
        {

            MemberForm form = new MemberForm();
            form.setName(userInfo.getName());
            form.setEmail(userInfo.getEmail());
            form.setCreateId(MemberForm.generateRandomId());

            session.setAttribute("memberForm", form);

            response.sendRedirect("/members/createMemberForm");
            return;
        }//@return

        response.sendRedirect("/");
    }

    @GetMapping("/oauth2/googleLogout")
    public void logoutFromGoogle(HttpSession session, HttpServletResponse response) throws IOException {

        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            googleUserService.logoutFromGoogle(accessToken);
        }

        // 세션 무효화
        session.invalidate();

        //return "redirect:/";
        response.sendRedirect("/");

    }

}
