    package tyml.reservationshop.service.util;

    import lombok.extern.slf4j.Slf4j;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.lang.Nullable;
    import org.springframework.security.authentication.AnonymousAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.web.savedrequest.RequestCache;
    import org.springframework.stereotype.Component;
    import org.springframework.web.servlet.HandlerInterceptor;
    import org.springframework.web.servlet.ModelAndView;

    @Slf4j
    @Component
    public class LoginInterceptor implements HandlerInterceptor {


        @Autowired
        private RequestCache requestCache;

        @Override
        public boolean preHandle(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Object handler) throws Exception {


            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {

                String requestedWithHeader = request.getHeader("X-Requested-With");
                boolean isAjaxRequest = "XMLHttpRequest".equals(requestedWithHeader);
                if (isAjaxRequest) {
                    // AJAX 요청에 대해 401 상태 코드를 반환
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                } else {
                    // 일반 요청에 대해 리다이렉트
                    request.getSession()
                            .setAttribute("redirectAfterLogin", request.getHeader("OriginURL"));
                    response.sendRedirect("/members/loginMemberPage");

                    //이전 페이지 저장,
                    requestCache.saveRequest(request, response);
                }
                return false;
            }

            return true;
        }


        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        }

    }
