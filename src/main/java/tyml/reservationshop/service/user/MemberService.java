package tyml.reservationshop.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.Reservation;
import tyml.reservationshop.domain.dto.MemberForm;
import tyml.reservationshop.domain.dto.MemberModifyForm;
import tyml.reservationshop.repository.MemberRepository;
import tyml.reservationshop.service.PlaceService;
import tyml.reservationshop.service.ReservationService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final ReservationService reservationService;
    private final PlaceService placeService;

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
    public void addReservation(Long memberId, Reservation reservation) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID : " + memberId));
        findMember.getReservations().add(reservation);
    }


    @Transactional
    public void deleteMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID : " + memberId));
        memberRepository.delete(findMember);
    }

    @Transactional
    public void deleteReservation(Long memberId, Long reservationId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID : " + memberId));

        Reservation findReservation = findMember.getReservations().stream()
                .filter(reservation -> reservation.getId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID : " + reservationId));

        findMember.getReservations().remove(findReservation);
        reservationService.deleteReservation(reservationId);
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
