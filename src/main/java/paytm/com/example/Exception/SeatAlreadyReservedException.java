package paytm.com.example.Exception;

public class SeatAlreadyReservedException extends RuntimeException {

    public SeatAlreadyReservedException(String message) {
        super(message);
    }
}