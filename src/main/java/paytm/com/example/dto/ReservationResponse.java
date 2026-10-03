package paytm.com.example.dto;

public class ReservationResponse {

	private Long reservationId;
	private Long showId;
	private String userId;
	private String seatNumber;
	private String status;

	public ReservationResponse(Long reservationId, Long showId, String userId, String seatNumber, String status) {

		this.reservationId = reservationId;
		this.showId = showId;
		this.userId = userId;
		this.seatNumber = seatNumber;
		this.status = status;
	}

	public Long getReservationId() {
		return reservationId;
	}

	public Long getShowId() {
		return showId;
	}

	public String getUserId() {
		return userId;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public String getStatus() {
		return status;
	}
}
