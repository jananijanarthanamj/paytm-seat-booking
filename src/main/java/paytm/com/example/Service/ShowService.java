package paytm.com.example.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import paytm.com.example.Entity.Reservation;
import paytm.com.example.Entity.ReservationStatus;
import paytm.com.example.Entity.Seat;
import paytm.com.example.Entity.SeatStatus;
import paytm.com.example.Entity.Show;
import paytm.com.example.Exception.SeatAlreadyReservedException;
import paytm.com.example.Exception.UserReservationLimitExceededException;
import paytm.com.example.Repository.ReservationRepository;
import paytm.com.example.Repository.SeatRepository;
import paytm.com.example.Repository.ShowRepository;
import paytm.com.example.dto.CreateShowRequest;
import paytm.com.example.dto.ReservationResponse;
import paytm.com.example.dto.ReserveRequest;
import paytm.com.example.dto.SeatResponse;
import paytm.com.example.dto.ShowResponse;

@Service
public class ShowService {

	private final ShowRepository showRepository;
	private final SeatRepository seatRepository;
	private final ReservationRepository reservationRepository;

	public ShowService(ShowRepository showRepository, SeatRepository seatRepository,
			ReservationRepository reservationRepository) {

		this.showRepository = showRepository;
		this.seatRepository = seatRepository;
		this.reservationRepository = reservationRepository;
	}

	@Transactional
	public Show createShow(CreateShowRequest request) {

		Show show = new Show(request.getName(), request.getSeats(), request.getPricePaise());

		Show savedShow = showRepository.save(show);

		for (int i = 1; i <= request.getSeats(); i++) {

			Seat seat = new Seat(savedShow, "A" + i, SeatStatus.AVAILABLE);

			seatRepository.save(seat);
		}

		return savedShow;
	}

	public ShowResponse getShow(Long showId) {

		Show show = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Show not found"));

		List<Seat> seats = seatRepository.findByShowId(showId);

		int availableSeats = 0;
		int confirmedSeats = 0;

		for (Seat seat : seats) {
			if (seat.getStatus() == SeatStatus.AVAILABLE) {
				availableSeats++;
			} else if (seat.getStatus() == SeatStatus.CONFIRMED) {
				confirmedSeats++;
			}
		}

		List<SeatResponse> seatResponses = seats.stream()
				.map(seat -> new SeatResponse(seat.getSeatNumber(), seat.getStatus().name()))
				.collect(Collectors.toList());

		return new ShowResponse(show.getId(), show.getName(), show.getPricePaise(), show.getTotalSeats(),
				availableSeats, confirmedSeats, seatResponses);
	}

	@Transactional
	public ReservationResponse reserveSeats(Long showId, ReserveRequest request) {

		Show show = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Show not found"));

		long currentReservations = reservationRepository.countByUserIdAndStatus(request.getUserId(),
				ReservationStatus.CONFIRMED);

		int requestedSeats = request.getSeats().size();

		if (currentReservations + requestedSeats > 4) {
			throw new UserReservationLimitExceededException("User reservation limit exceeded");
		}

		List<ReservationResponse> responses = new ArrayList<>();

		for (String seatNumber : request.getSeats()) {

			Seat seat = seatRepository.findByShowId(showId).stream().filter(s -> s.getSeatNumber().equals(seatNumber))
					.findFirst().orElseThrow(() -> new RuntimeException("Seat not found"));

			if (seat.getStatus() == SeatStatus.CONFIRMED) {
				throw new SeatAlreadyReservedException("Seat already confirmed");
			}

			seat.setStatus(SeatStatus.CONFIRMED);

			Reservation reservation = new Reservation(request.getUserId(), show, seat, ReservationStatus.CONFIRMED);

			Reservation savedReservation = reservationRepository.save(reservation);

			responses.add(new ReservationResponse(savedReservation.getId(), show.getId(), request.getUserId(),
					seat.getSeatNumber(), savedReservation.getStatus().name()));
		}

		return responses.get(0);
	}

}
