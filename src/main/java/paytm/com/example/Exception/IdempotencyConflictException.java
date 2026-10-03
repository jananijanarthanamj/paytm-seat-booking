package paytm.com.example.Exception;

public class IdempotencyConflictException extends RuntimeException{

	public IdempotencyConflictException(String message) {
        super(message);
    }
	
}
