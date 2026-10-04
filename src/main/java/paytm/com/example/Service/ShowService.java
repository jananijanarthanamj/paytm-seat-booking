package paytm.com.example.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import paytm.com.example.Entity.IdempotencyRecord;
import paytm.com.example.Entity.Reservation;
import paytm.com.example.Entity.ReservationStatus;
import paytm.com.example.Entity.Seat;
import paytm.com.example.Entity.SeatStatus;
import paytm.com.example.Entity.Show;
import paytm.com.example.Exception.IdempotencyConflictException;
import paytm.com.example.Exception.InvalidReservationRequestException;
import paytm.com.example.Exception.MissingIdempotencyKeyException;
import paytm.com.example.Exception.OriginalReservationNotFoundException;
import paytm.com.example.Exception.ReservationAlreadyCancelledException;
import paytm.com.example.Exception.ReservationNotFoundException;
import paytm.com.example.Exception.SeatAlreadyReservedException;
import paytm.com.example.Exception.SeatNotFoundException;
import paytm.com.example.Exception.ShowNotFoundException;
import paytm.com.example.Exception.UnauthorizedReservationCancellationException;
import paytm.com.example.Exception.UserLockNotFoundException;
import paytm.com.example.Exception.UserReservationLimitExceededException;
import paytm.com.example.Repository.IdempotencyRecordRepository;
import paytm.com.example.Repository.ReservationRepository;
import paytm.com.example.Repository.SeatRepository;
import paytm.com.example.Repository.ShowRepository;
import paytm.com.example.Repository.UserReservationLockRepository;
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
	private final IdempotencyRecordRepository idempotencyRecordRepository;
	private final UserReservationLockRepository userReservationLockRepository;

	public ShowService(ShowRepository showRepository, SeatRepository seatRepository,
			ReservationRepository reservationRepository, IdempotencyRecordRepository idempotencyRecordRepository,
			UserReservationLockRepository userReservationLockRepository) {

		this.showRepository = showRepository;
		this.seatRepository = seatRepository;
		this.reservationRepository = reservationRepository;
		this.idempotencyRecordRepository = idempotencyRecordRepository;
		this.userReservationLockRepository = userReservationLockRepository;
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

		Show show = showRepository.findById(showId).orElseThrow(() -> new ShowNotFoundException("Show not found"));

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
	public ReservationResponse reserveSeats(Long showId, ReserveRequest request, String userId) {

		if (request.getSeats() == null || request.getSeats().isEmpty()) {
			throw new InvalidReservationRequestException("At least one seat is required");
		}

		Set<String> uniqueSeats = new HashSet<>(request.getSeats());

		if (uniqueSeats.size() != request.getSeats().size()) {
			throw new InvalidReservationRequestException("Duplicate seats are not allowed");
		}

		if (request.getIdempotencyKey() == null || request.getIdempotencyKey().isBlank()) {

			throw new MissingIdempotencyKeyException("Idempotency key is required");
		}

		Optional<IdempotencyRecord> existingRecord = idempotencyRecordRepository
				.findByIdempotencyKey(request.getIdempotencyKey());

		if (existingRecord.isPresent()) {

			IdempotencyRecord record = existingRecord.get();

			String requestedSeats = String.join(",", request.getSeats());

			if (!record.getUserId().equals(userId) || !record.getShowId().equals(showId)
					|| !record.getSeatNumbers().equals(requestedSeats)) {

				throw new IdempotencyConflictException("Idempotency key already used with different request");
			}

			Reservation reservation = reservationRepository.findById(record.getReservationId())
					.orElseThrow(() -> new OriginalReservationNotFoundException("Original reservation not found"));

			return new ReservationResponse(reservation.getId(), reservation.getShow().getId(), reservation.getUserId(),
					reservation.getSeat().getSeatNumber(), reservation.getStatus().name());
		}

		Show show = showRepository.findById(showId).orElseThrow(() -> new ShowNotFoundException("Show not found"));

		userReservationLockRepository.createLockIfNotExists(userId);

		userReservationLockRepository.findById(userId).orElseThrow(() -> new UserLockNotFoundException("User lock not found"));

		List<Reservation> existingReservations = reservationRepository.findByUserIdAndStatus(userId,
				ReservationStatus.CONFIRMED);

		int currentReservations = existingReservations.size();
		int requestedSeats = request.getSeats().size();

		if (currentReservations + requestedSeats > 4) {
			throw new UserReservationLimitExceededException("User reservation limit exceeded");
		}

		List<ReservationResponse> responses = new ArrayList<>();

		Long firstReservationId = null;

		for (String seatNumber : request.getSeats()) {

			Seat seat = seatRepository.findByShowIdAndSeatNumber(showId, seatNumber)
					.orElseThrow(() -> new SeatNotFoundException("Seat not found"));

			if (seat.getStatus() == SeatStatus.CONFIRMED) {

				throw new SeatAlreadyReservedException("Seat already confirmed");
			}

			seat.setStatus(SeatStatus.CONFIRMED);

			Reservation reservation = new Reservation(userId, show, seat, ReservationStatus.CONFIRMED,
					request.getIdempotencyKey());

			Reservation savedReservation = reservationRepository.save(reservation);

			if (firstReservationId == null) {
				firstReservationId = savedReservation.getId();
			}

			responses.add(new ReservationResponse(savedReservation.getId(), show.getId(), reservation.getUserId(),
					seat.getSeatNumber(), savedReservation.getStatus().name()));
		}

		// Save ONE idempotency record for the whole request
		IdempotencyRecord record = new IdempotencyRecord(request.getIdempotencyKey(), userId, showId,
				String.join(",", request.getSeats()), firstReservationId);

		idempotencyRecordRepository.save(record);

		return responses.get(0);
	}

	@Transactional
	public void cancelReservation(Long reservationId, String userId) {

		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

		if (!reservation.getUserId().equals(userId)) {
			throw new UnauthorizedReservationCancellationException("User is not allowed to cancel this reservation");
		}

		if (reservation.getStatus() == ReservationStatus.CANCELLED) {
			throw new ReservationAlreadyCancelledException("Reservation already cancelled");
		}

		Seat seat = reservation.getSeat();

		reservation.setStatus(ReservationStatus.CANCELLED);
		seat.setStatus(SeatStatus.AVAILABLE);
	}

}
