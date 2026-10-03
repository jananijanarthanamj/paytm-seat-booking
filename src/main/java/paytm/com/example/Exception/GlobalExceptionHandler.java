package paytm.com.example.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SeatAlreadyReservedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleSeatAlreadyReserved(SeatAlreadyReservedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserReservationLimitExceededException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserReservationLimitExceeded(
            UserReservationLimitExceededException ex) {
        return ex.getMessage();
    }
}
