package paytm.com.example.dto;

import java.util.List;

public class ShowResponse {

    private Long id;
    private String name;
    private long pricePaise;
    private int totalSeats;
    private int availableSeats;
    private int confirmedSeats;
    private List<SeatResponse> seats;

    public ShowResponse(
            Long id,
            String name,
            long pricePaise,
            int totalSeats,
            int availableSeats,
            int confirmedSeats,
            List<SeatResponse> seats) {

        this.id = id;
        this.name = name;
        this.pricePaise = pricePaise;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.confirmedSeats = confirmedSeats;
        this.seats = seats;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPricePaise() {
        return pricePaise;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public int getConfirmedSeats() {
        return confirmedSeats;
    }

    public List<SeatResponse> getSeats() {
        return seats;
    }
}
