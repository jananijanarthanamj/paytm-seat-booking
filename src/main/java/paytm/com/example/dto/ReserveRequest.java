package paytm.com.example.dto;

import java.util.List;

public class ReserveRequest {

	private String userId;
	private List<String> seats;
	private String idempotencyKey;

	public String getUserId() {
		return userId;
	}

	public List<String> getSeats() {
		return seats;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
