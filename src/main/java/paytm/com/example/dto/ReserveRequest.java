package paytm.com.example.dto;

import java.util.List;

public class ReserveRequest {

	private List<String> seats;
	private String idempotencyKey;

	public List<String> getSeats() {
		return seats;
	}

	public void setSeats(List<String> seats) {
		this.seats = seats;
	}

	public String getIdempotencyKey() {
		return idempotencyKey;
	}

	public void setIdempotencyKey(String idempotencyKey) {
		this.idempotencyKey = idempotencyKey;
	}

}
