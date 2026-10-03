package paytm.com.example.dto;

import java.util.List;

public class ReserveRequest {

	private String userId;
	private List<String> seats;

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

}
