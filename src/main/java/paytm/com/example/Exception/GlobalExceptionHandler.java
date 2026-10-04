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
    
    @ExceptionHandler(IdempotencyConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleIdempotencyConflict(
            IdempotencyConflictException ex) {

        return ex.getMessage();
    }
    
    @ExceptionHandler(UnauthorizedReservationCancellationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUnauthorizedReservationCancellation(
            UnauthorizedReservationCancellationException ex) {

        return ex.getMessage();
    }
    
    @ExceptionHandler(MissingIdempotencyKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingIdempotencyKey(
            MissingIdempotencyKeyException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(ShowNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleShowNotFound(ShowNotFoundException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(SeatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleSeatNotFound(SeatNotFoundException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(InvalidReservationRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidReservationRequest(
            InvalidReservationRequestException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleReservationNotFound(
            ReservationNotFoundException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(OriginalReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleOriginalReservationNotFound(
            OriginalReservationNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserLockNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUserLockNotFound(
            UserLockNotFoundException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(ReservationAlreadyCancelledException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleReservationAlreadyCancelled(
            ReservationAlreadyCancelledException ex) {
        return ex.getMessage();
    }
}
