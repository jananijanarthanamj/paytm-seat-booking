package paytm.com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import paytm.com.example.Entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long>  {
	
	List<Seat> findByShowId(Long showId);

}
