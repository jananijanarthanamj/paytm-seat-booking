package paytm.com.example.Service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import paytm.com.example.Entity.Seat;
import paytm.com.example.Entity.SeatStatus;
import paytm.com.example.Entity.Show;
import paytm.com.example.Repository.SeatRepository;
import paytm.com.example.Repository.ShowRepository;
import paytm.com.example.dto.CreateShowRequest;

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
}
