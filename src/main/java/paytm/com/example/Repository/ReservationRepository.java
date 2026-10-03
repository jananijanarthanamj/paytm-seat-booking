package paytm.com.example.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import paytm.com.example.Entity.Reservation;
import paytm.com.example.Entity.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	long countByUserIdAndStatus(String userId, ReservationStatus status);
	Optional<Reservation> findByIdempotencyKey(String idempotencyKey);
}
