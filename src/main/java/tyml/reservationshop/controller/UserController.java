package tyml.reservationshop.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.service.GoogleUserService;
import tyml.reservationshop.service.MemberService;
import tyml.reservationshop.service.factory.UserServiceFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;
    private final UserServiceFactory userServiceFactory;


    @GetMapping("/oauth2/callback/{provider}")
    public void Login(@PathVariable("provider") String provider,
                      @RequestParam Map<String, String> params,
                      HttpSession session,
                      HttpServletResponse response) throws IOException
    {

        String code = params.get("code");
        String state = params.get("state");

        String accessToken = userServiceFactory.getUserService(provider).getAccessToken(code, state);

        String userName = userServiceFactory.getUserName(provider, accessToken);
        String userEmail = userServiceFactory.getUserEmail(provider, accessToken);

        session.setAttribute("provider", provider);
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("userEmail", userEmail);

        Optional<Member> member = Optional.ofNullable(memberService.findByEmail(userEmail));
        if(member.isEmpty()) // 이메일 없으면 계정 생성 페이지 전환
        {
            MemberForm form = new MemberForm();
            form.setName(userName);
            form.setEmail(userEmail);

            session.setAttribute("memberForm", form);
            response.sendRedirect("/members/createMemberForm");
            return;
        }

        response.sendRedirect("/");
    }

    @GetMapping("/oauth2/logout")
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {

        String provider = (String) session.getAttribute("provider");
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            userServiceFactory.getUserService(provider).logout(accessToken);
        }

        session.invalidate();

        response.sendRedirect("/");
    }

}
