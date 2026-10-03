package paytm.com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import paytm.com.example.Entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long>  {

}
