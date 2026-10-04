package paytm.com.example.Exception;

public class UserLockNotFoundException extends RuntimeException {

    public UserLockNotFoundException(String message) {
        super(message);
    }
}