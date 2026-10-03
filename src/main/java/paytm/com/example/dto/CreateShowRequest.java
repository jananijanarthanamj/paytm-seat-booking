package paytm.com.example.dto;

public class CreateShowRequest {


    private String name;
    private int seats;
    private long pricePaise;

    public String getName() {
        return name;
    }

    public int getSeats() {
        return seats;
    }

    public long getPricePaise() {
        return pricePaise;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setPricePaise(long pricePaise) {
        this.pricePaise = pricePaise;
    }
    
}
