package tyml.reservationshop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Member;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByName(String name);

    Member findByEmail(String email);

}
