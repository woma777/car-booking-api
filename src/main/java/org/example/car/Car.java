package org.example.car;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID carId;
    @Column(nullable = false, unique = true)
    private String regNumber;
    @Column(nullable = false)
    private BigDecimal rentalPricePerDay;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Brand brand;
    @Column(nullable = false)
    private boolean isElectric;
    public Car() {
    }
    public Car(String regNumber, BigDecimal rentalPricePerDay, Brand brand, boolean isElectric) {

        this.regNumber = regNumber;
        this.rentalPricePerDay = rentalPricePerDay;
        this.brand = brand;
        this.isElectric = isElectric;
    }
    public UUID getCarId() {
        return carId;
    }
    public void setCarId(UUID carId) {
        this.carId = carId;
    }
    public String getRegNumber() {
        return regNumber;
    }
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }
    public BigDecimal getRentalPricePerDay() {
        return rentalPricePerDay;
    }
    public void setRentalPricePerDay(BigDecimal rentalPricePerDay) {
        this.rentalPricePerDay = rentalPricePerDay;
    }
    public Brand getBrand() {
        return brand;
    }
    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    public boolean isElectric() {
        return isElectric;
    }
    public void setElectric(boolean electric) {
        isElectric = electric;
    }
    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", regNumber='" + regNumber + '\'' +
                ", rentalPricePerDay=" + rentalPricePerDay +
                ", brand=" + brand +
                ", isElectric=" + isElectric +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(carId, car.carId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(carId);
    }
}
