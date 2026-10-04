package paytm.com.example.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;
import paytm.com.example.Entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long>  {
	
	List<Seat> findByShowId(Long showId);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Seat> findByShowIdAndSeatNumber(Long showId, String seatNumber);

}
