package tyml.reservationshop.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.domain.dto.MemberModifyForm;
import tyml.reservationshop.repository.MemberRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //회원 가입
    @Transactional
    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public void updateMember(Long memberId, MemberModifyForm memberForm) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID : " + memberId));
        findMember.UpdateFromMemberModifyForm(memberForm);
    }

    @Transactional
    public void updatePassword(Long memberId, String changePw){
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID : " + memberId));
        findMember.setPw(passwordEncoder.encode(changePw));
    }


    @Transactional
    public void deleteMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID : " + memberId));
        memberRepository.delete(findMember);
    }

    //회원 전체 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findOne(Long member_id) {
        return memberRepository.findById(member_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID : " + member_id));
    }

    public List<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
