package paytm.com.example.Exception;

public class InvalidReservationRequestException extends RuntimeException {

    public InvalidReservationRequestException(String message) {
        super(message);
    }
}