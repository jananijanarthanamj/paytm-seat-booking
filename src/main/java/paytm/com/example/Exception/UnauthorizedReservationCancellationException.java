package paytm.com.example.Exception;

public class UnauthorizedReservationCancellationException extends RuntimeException {
	public UnauthorizedReservationCancellationException(String message) {
		super(message);
	}
}
