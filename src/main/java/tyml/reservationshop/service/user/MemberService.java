package tyml.reservationshop.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.repository.MemberRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional
    public Long join(Member member) {
        //validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public void updateMember(Long memberId, MemberForm memberForm) {

        Member findMember = memberRepository.findOne(memberId);
        findMember.UpdateFromMemberForm(memberForm);
    }


    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteMemberByMemberId(memberId);
    }

    //회원 전체 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findOne(Long member_id) {
        return memberRepository.findOne(member_id);
    }

    public List<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
