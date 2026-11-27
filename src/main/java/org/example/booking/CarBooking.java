package org.example.booking;
import jakarta.persistence.*;
import org.example.car.Car;
import org.example.user.AppUser;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
@Entity
@Table(name = "car_booking")
public class CarBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookingId;
    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser user;
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
    @Column(nullable = false)
    private LocalDateTime bookedAt;
    public CarBooking() {
    }
    public CarBooking( AppUser user, Car car, LocalDate startDate, LocalDate endDate,
                      BigDecimal price, BookingStatus status, LocalDateTime bookedAt) {
        this.user = user;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.status = status;
        this.bookedAt = bookedAt;
    }

    public UUID getBookingId() {
        return bookingId;
    }
    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }
    public AppUser getUser() {
        return user;
    }
    public void setUser(AppUser user) {
        this.user = user;
    }
    public Car getCar() {
        return car;
    }
    public void setCar(Car car) {
        this.car = car;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public BookingStatus getStatus() {
        return status;
    }
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    public LocalDateTime getBookedAt() {
        return bookedAt;
    }
    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
    @Override
    public String toString() {
        return "CarBooking{" +
                "bookingId=" + bookingId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", status=" + status +
                ", bookedAt=" + bookedAt +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CarBooking that = (CarBooking) o;
        return Objects.equals(bookingId, that.bookingId);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(bookingId);
    }
}
