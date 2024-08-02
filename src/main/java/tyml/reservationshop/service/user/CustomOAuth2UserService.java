package tyml.reservationshop.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Address;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.domain.dto.user.*;
import tyml.reservationshop.domain.enums.UserRole;
import tyml.reservationshop.repository.MemberRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }


    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerTypeCode = provider.toUpperCase();

        Oauth2UserInfo userInfo = null;
        if (provider.equals("kakao")) {
            userInfo = new KakaoUserInfo(oAuth2User.getAttribute("kakao_account"));
        }
        else if (provider.equals("naver")) {
            userInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }
        else if (provider.equals("google")) {
            userInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }


        String username = providerTypeCode + userInfo.getName();
        String email = userInfo.getEmail();

        Member member = null;
        Optional<Member> optionalSiteUser = Optional.ofNullable(this.memberRepository.findByEmail(email));
        if (optionalSiteUser.isEmpty()) {

            MemberForm form = new MemberForm();
            form.setName(username);
            form.setEmail(email);
            form.setPw("1234"); //TODO : 임의 비밀번호

            member = new Member(form);
            memberRepository.save(member); //@DB 저장
        } else {
            member = optionalSiteUser.get();
        }


        GrantedAuthority authority = new SimpleGrantedAuthority(UserRole.USER.getValue());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);

        return new CustomOAuth2User(member.getEmail(), member.getPw(), authorities);
    }
}
