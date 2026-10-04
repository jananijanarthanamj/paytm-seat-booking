package paytm.com.example.Exception;

public class ReservationAlreadyCancelledException extends RuntimeException {

    public ReservationAlreadyCancelledException(String message) {
        super(message);
    }
}