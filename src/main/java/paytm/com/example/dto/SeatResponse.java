package paytm.com.example.dto;

public class SeatResponse {

    private String seatNumber;
    private String status;

    public SeatResponse(String seatNumber, String status) {
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getStatus() {
        return status;
    }
}
