package paytm.com.example.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import paytm.com.example.Entity.Seat;
import paytm.com.example.Entity.SeatStatus;
import paytm.com.example.Entity.Show;
import paytm.com.example.Repository.SeatRepository;
import paytm.com.example.Repository.ShowRepository;
import paytm.com.example.dto.CreateShowRequest;
import paytm.com.example.dto.SeatResponse;
import paytm.com.example.dto.ShowResponse;

@Service
public class ShowService {


    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    public ShowService(
            ShowRepository showRepository,
            SeatRepository seatRepository) {

        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public Show createShow(CreateShowRequest request) {

        Show show = new Show(
                request.getName(),
                request.getSeats(),
                request.getPricePaise()
        );

        Show savedShow = showRepository.save(show);

        for (int i = 1; i <= request.getSeats(); i++) {

            Seat seat = new Seat(
                    savedShow,
                    "A" + i,
                    SeatStatus.AVAILABLE
            );

            seatRepository.save(seat);
        }

        return savedShow;
    }
    
    public ShowResponse getShow(Long showId) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

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
                .map(seat -> new SeatResponse(
                        seat.getSeatNumber(),
                        seat.getStatus().name()
                ))
                .collect(Collectors.toList());

        return new ShowResponse(
                show.getId(),
                show.getName(),
                show.getPricePaise(),
                show.getTotalSeats(),
                availableSeats,
                confirmedSeats,
                seatResponses
        );
    }
    
}
