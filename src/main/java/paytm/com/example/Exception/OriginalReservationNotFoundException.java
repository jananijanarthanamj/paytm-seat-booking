package paytm.com.example.Exception;

public class OriginalReservationNotFoundException extends RuntimeException {

    public OriginalReservationNotFoundException(String message) {
        super(message);
    }
}