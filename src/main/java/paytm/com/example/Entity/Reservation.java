package paytm.com.example.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "show_id", nullable = false)
	private Show show;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "seat_id", nullable = false)
	private Seat seat;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReservationStatus status;

	@Column(name = "idempotency_key", nullable = false)
	private String idempotencyKey;

	public Reservation() {
	}

	public Reservation(String userId, Show show, Seat seat, ReservationStatus status, String idempotencyKey) {
		this.userId = userId;
		this.show = show;
		this.seat = seat;
		this.status = status;
		this.idempotencyKey = idempotencyKey;
	}

	public Long getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public Show getShow() {
		return show;
	}

	public Seat getSeat() {
		return seat;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
	
	public String getIdempotencyKey() {
	    return idempotencyKey;
	}

}
