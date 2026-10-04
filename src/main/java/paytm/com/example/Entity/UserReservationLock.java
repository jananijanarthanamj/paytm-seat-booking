package paytm.com.example.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_reservation_locks")
public class UserReservationLock {

    @Id
    private String userId;

    public UserReservationLock() {
    }

    public UserReservationLock(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}