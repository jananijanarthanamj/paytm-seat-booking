package paytm.com.example.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "idempotency_records",
       uniqueConstraints = @UniqueConstraint(columnNames = "idempotency_key"))
public class IdempotencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "show_id", nullable = false)
    private Long showId;

    @Column(name = "seat_numbers", nullable = false)
    private String seatNumbers;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    public IdempotencyRecord() {
    }

    public IdempotencyRecord(String idempotencyKey, String userId,
                             Long showId, String seatNumbers,
                             Long reservationId) {
        this.idempotencyKey = idempotencyKey;
        this.userId = userId;
        this.showId = showId;
        this.seatNumbers = seatNumbers;
        this.reservationId = reservationId;
    }

    public Long getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getUserId() {
        return userId;
    }

    public Long getShowId() {
        return showId;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public Long getReservationId() {
        return reservationId;
    }
}