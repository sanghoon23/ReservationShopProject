package tyml.reservationshop.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Address;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.repository.MemberRepository;
import tyml.reservationshop.service.user.MemberService;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    @Autowired private EntityManager em;

    @Test
    public void 회원가입_테스트() {

        MemberForm form = new MemberForm();
        Address address = new Address("123", "서울특별시","123");
        Member member = new Member(form);
        memberService.join(member);
    }

    @Test
    public void 회원목록_테스트()  {

    }




}