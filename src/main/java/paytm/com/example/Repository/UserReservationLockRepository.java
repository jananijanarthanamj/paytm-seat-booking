package paytm.com.example.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import paytm.com.example.Entity.UserReservationLock;

public interface UserReservationLockRepository
        extends JpaRepository<UserReservationLock, String> {

    @Modifying
    @Query(
        value = "INSERT IGNORE INTO user_reservation_locks (user_id) VALUES (:userId)",
        nativeQuery = true
    )
    void createLockIfNotExists(String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserReservationLock> findById(String userId);
}