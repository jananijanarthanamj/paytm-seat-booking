package paytm.com.example.Exception;

public class UserReservationLimitExceededException extends RuntimeException {

    public UserReservationLimitExceededException(String message) {
        super(message);
    }
}