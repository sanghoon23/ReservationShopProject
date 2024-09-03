package tyml.reservationshop.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.domain.Reservation;
import tyml.reservationshop.repository.ReservationRepository;

import java.util.List;

@Service
@Getter
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional
    public Long join(Reservation reservation) {
        reservationRepository.save(reservation);
        return reservation.getId();
    }

    @Transactional
    public void deleteReservation(Long reservationId) {
        Reservation findReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID : " + reservationId));
        reservationRepository.delete(findReservation);
    }


    public Reservation findOne(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID : " + reservationId));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

}
