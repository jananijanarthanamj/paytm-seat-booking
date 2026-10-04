package paytm.com.example.Exception;

public class SeatNotFoundException extends RuntimeException {

    public SeatNotFoundException(String message) {
        super(message);
    }
}