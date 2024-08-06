package tyml.reservationshop.service.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 세션에서 원래 URL 가져오기
        String redirectUrl = (String) request.getSession().getAttribute("redirectAfterLogin");

        // URL이 없으면 기본 URL로 리다이렉트
        if (redirectUrl == null) {
            redirectUrl = "/";
        }

        // 세션에서 URL 제거
        request.getSession().removeAttribute("redirectAfterLogin");

        // 최종 리다이렉트
        response.sendRedirect(redirectUrl);
    }

}
