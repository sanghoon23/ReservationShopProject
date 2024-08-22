package tyml.reservationshop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.repository.MemberRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Value("${admin.id}")
    private String adminId;

    @Value("${admin.password}")
    private String adminPassword;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Admin 계정이 존재하지 않을 경우 추가
        if (memberRepository.findByEmail("admin@ReservationShop.com") == null) {

            MemberForm form = new MemberForm();
            form.setName("관리자");
            form.setEmail(adminId);
            form.setPw(adminPassword); //관리자 비밀번호
            form.setPw(passwordEncoder.encode(form.getPw()));

            Member member = new Member(form);
            memberRepository.save(member); //@DB 저장

        }
    }

}
