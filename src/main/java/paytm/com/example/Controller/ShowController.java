package paytm.com.example.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import paytm.com.example.Entity.Show;
import paytm.com.example.Security.AuthenticatedUserFilter;
import paytm.com.example.Service.ShowService;
import paytm.com.example.dto.CreateShowRequest;
import paytm.com.example.dto.ReservationResponse;
import paytm.com.example.dto.ReserveRequest;
import paytm.com.example.dto.ShowResponse;

@RestController
@RequestMapping("/shows")
public class ShowController {

	private final ShowService showService;

	public ShowController(ShowService showService) {
		this.showService = showService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Show createShow(@RequestBody CreateShowRequest request) {
		return showService.createShow(request);
	}

	@GetMapping("/{id}")
	public ShowResponse getShow(@PathVariable Long id) {
		return showService.getShow(id);
	}

	@PostMapping("/{id}/reserve")
	@ResponseStatus(HttpStatus.CREATED)
	public ReservationResponse reserveSeats(
	        @PathVariable Long id,
	        @RequestBody ReserveRequest request,
	        HttpServletRequest httpRequest) {

	    String userId = (String) httpRequest.getAttribute(
	            AuthenticatedUserFilter.USER_ID_ATTRIBUTE
	    );

	    return showService.reserveSeats(id, request, userId);
	}

	@PostMapping("/reservations/{reservationId}/cancel")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelReservation(
	        @PathVariable Long reservationId,
	        HttpServletRequest httpRequest) {

	    String userId = (String) httpRequest.getAttribute(
	            AuthenticatedUserFilter.USER_ID_ATTRIBUTE
	    );

	    showService.cancelReservation(reservationId, userId);
	}
}
