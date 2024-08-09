package tyml.reservationshop.service.util;

import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {


        log.warn("인증 정보가 없음. 로그인 페이지로 리다이렉트됩니다.");
        log.info("request uri : {}", request.getRequestURI());
        log.info("request getHeader uri : {}", request.getHeader("OriginURL"));

        String requestedWithHeader = request.getHeader("X-Requested-With");
        boolean isAjaxRequest = "XMLHttpRequest".equals(requestedWithHeader);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {

            if (isAjaxRequest) {
                // AJAX 요청에 대해 401 상태 코드를 반환
                log.info("isAjaxRequest IN!!!");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                // 일반 요청에 대해 리다이렉트
                log.info("normal Request IN!!");
                request.getSession()
                        .setAttribute("redirectAfterLogin", request.getHeader("OriginURL"));
                response.sendRedirect("/members/loginMemberPage");
            }
            return false;
        }

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        System.out.println("Post Handle: " + request.getRequestURI());

        log.warn("Post Handle IN!!.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {

            log.info("authentication IN!!");
            request.getSession()
                    .setAttribute("redirectAfterLogin", request.getHeader("OriginURL"));
            response.sendRedirect("/members/loginMemberPage");
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("After Completion: " + request.getRequestURI());
        if (ex != null) {
            ex.printStackTrace();
        }
    }

}
