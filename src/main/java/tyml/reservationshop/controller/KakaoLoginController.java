package tyml.reservationshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.KakaoUserInfoResponseDto;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.service.KakaoLoginService;
import tyml.reservationshop.service.MemberService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final MemberService memberService;
    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/oauth2/callback/kakao")
    public void kakaoLogin(@RequestParam("code") String code, HttpSession session, HttpServletResponse response) throws IOException {
        String accessToken = kakaoLoginService.getAccessTokenFromKakao(code);

        KakaoUserInfoResponseDto userInfo = kakaoLoginService.getUserInfo(accessToken);

        String userName = userInfo.getKakaoAccount().getProfile().getNickName();
        String userEmail = userInfo.getKakaoAccount().email;

        session.setAttribute("accessToken", accessToken);
        session.setAttribute("userEmail", userEmail);

        Optional<Member> member = Optional.ofNullable(memberService.findByEmail(userEmail));
        if(member.isEmpty()) // 이메일 없으면 계정 생성 페이지 전환
        {

            MemberForm form = new MemberForm();
            form.setName(userInfo.getKakaoAccount().getProfile().getNickName());
            form.setEmail(userInfo.getKakaoAccount().email);
            form.setCreateId(MemberForm.generateRandomId());

            session.setAttribute("memberForm", form);

            response.sendRedirect("/members/createMemberForm");
            return;
        }//@return


        //return new ResponseEntity<>(HttpStatus.OK);
        //return "redirect:/";
        response.sendRedirect("/");
    }

    @GetMapping("/oauth2/kakaoLogout")
    public void kakaoLogout(HttpSession session, HttpServletResponse response) throws IOException {

        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            kakaoLoginService.logoutFromKakao(accessToken);
        }

        // 세션 무효화
        session.invalidate();

        //return "redirect:/";
        response.sendRedirect("/");
    }

    @GetMapping("/oauth2/kakaoUnlink")
    public void kakaoUnlink(HttpSession session, HttpServletResponse response) throws IOException {

        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            kakaoLoginService.unlinkFromKakao(accessToken);
        }

        // 세션 무효화
        session.invalidate();

        //return "redirect:/";
        response.sendRedirect("/");
    }

}
