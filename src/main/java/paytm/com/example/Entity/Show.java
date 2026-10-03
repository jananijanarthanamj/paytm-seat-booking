package paytm.com.example.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shows")
public class Show {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "total_seats", nullable = false)
    private int totalSeats;

    @Column(name = "price_paise", nullable = false)
    private long pricePaise;

    public Show() {
    }

    public Show(String name, int totalSeats, long pricePaise) {
        this.name = name;
        this.totalSeats = totalSeats;
        this.pricePaise = pricePaise;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public long getPricePaise() {
        return pricePaise;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setPricePaise(long pricePaise) {
        this.pricePaise = pricePaise;
    }

}
