package paytm.com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import paytm.com.example.Entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>  {

}
