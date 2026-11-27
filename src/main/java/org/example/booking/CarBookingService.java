package org.example.booking;
import org.example.car.Car;
import org.example.car.CarRepository;
import org.example.exception.DuplicateResourceException;
import org.example.exception.InvalidBookingDateException;
import org.example.exception.ResourceNotFundException;
import org.example.user.AppUser;
import org.example.user.AppUserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarBookingService {
    private final CarBookingRepository carBookingRepository;
    private final AppUserRepository appUserRepository;
    private final CarRepository carRepository;
    public CarBookingService(CarBookingRepository carBookingRepository,
                             AppUserRepository appUserRepository
            , CarRepository carRepository) {
        this.carBookingRepository = carBookingRepository;
        this.appUserRepository = appUserRepository;
        this.carRepository = carRepository;
    }
    public CarBookingResponse book(CarBookingRequest carBookingRequest) {
        AppUser user = appUserRepository.findById(carBookingRequest.userId())
                .orElseThrow(() -> new ResourceNotFundException("User with this id :"
                        + carBookingRequest.userId() + " is not found"));
        Car car = carRepository.findById(carBookingRequest.carId())
                .orElseThrow(() ->
                        new ResourceNotFundException(
                                "Car not found in this id:" + carBookingRequest.carId()
                        )
                );
        if (carBookingRequest.startDate().isBefore(LocalDate.now())) {
            throw new InvalidBookingDateException(
                    "Start date cannot be in the past");
        }
        if (!carBookingRequest.endDate().isAfter(carBookingRequest.startDate())) {
            throw new InvalidBookingDateException(
                    "End date must be after start date");
        }
        List<CarBooking> bookings = carBookingRepository.findAll();
        boolean isCarBooked = bookings.stream()
                .anyMatch(booking ->
                        booking.getCar().getCarId().equals(carBookingRequest.carId())
                                && booking.getStatus() == BookingStatus.ACTIVE
                                && carBookingRequest.startDate().isBefore(booking.getEndDate())
                                && booking.getStartDate().isBefore(carBookingRequest.endDate())
                );
        if (isCarBooked) {
            throw new DuplicateResourceException(
                    "The selected car is already booked");
        }
        long numberOfDays = ChronoUnit.DAYS.between(
                carBookingRequest.startDate(), carBookingRequest.endDate());
        BigDecimal totalPrice = car.getRentalPricePerDay()
                .multiply(BigDecimal.valueOf(numberOfDays));
        CarBooking booking = new CarBooking(
                user,
                car,
                carBookingRequest.startDate(),
                carBookingRequest.endDate(),
                totalPrice,
                BookingStatus.ACTIVE,
                LocalDateTime.now()
        );
        CarBooking savedBooking =carBookingRepository.save(booking);
        return new CarBookingResponse(
                savedBooking.getBookingId(),
                savedBooking.getUser().getName(),
                savedBooking.getCar().getRegNumber(),
                savedBooking.getCar().getBrand(),
                savedBooking.getStartDate(),
                savedBooking.getEndDate(),
                savedBooking.getPrice(),
                savedBooking.getStatus(),
                savedBooking.getBookedAt()
        );
    }
    public List<CarBookingResponse> getAllCarBookings()  {
        return carBookingRepository.findAll()
                .stream()
                .map(carBooking -> new CarBookingResponse(
                        carBooking.getBookingId(),
                        carBooking.getUser().getName(),
                        carBooking.getCar().getRegNumber(),
                        carBooking.getCar().getBrand(),
                        carBooking.getStartDate(),
                        carBooking.getEndDate(),
                        carBooking.getPrice(),
                        carBooking.getStatus(),
                        carBooking.getBookedAt()

                ))
                .collect(Collectors.toList());
    }
    public CarBookingResponse getCarBookingByBookingId(UUID bookingId) {
        CarBooking carBooking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFundException(
                        "CarBooking not found with id: " + bookingId
                ));
        return new CarBookingResponse(
                carBooking.getBookingId(),
                carBooking.getUser().getName(),
                carBooking.getCar().getRegNumber(),
                carBooking.getCar().getBrand(),
                carBooking.getStartDate(),
                carBooking.getEndDate(),
                carBooking.getPrice(),
                carBooking.getStatus(),
                carBooking.getBookedAt()
        );
    }
    public List<CarBookingResponse> allCarBookedByUserId(UUID userId){
     appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFundException("User with this id :" + userId + " is not found"));
        return  carBookingRepository.findByUser_UserId(userId)
                .stream()
                .map(carBooking -> new CarBookingResponse(
                        carBooking.getBookingId(),
                        carBooking.getUser().getName(),
                        carBooking.getCar().getRegNumber(),
                        carBooking.getCar().getBrand(),
                        carBooking.getStartDate(),
                        carBooking.getEndDate(),
                        carBooking.getPrice(),
                        carBooking.getStatus(),
                        carBooking.getBookedAt()
                ))
                .collect(Collectors.toList());
    }
    public void deleteCarBooking(UUID bookingId) {
        boolean exists = carBookingRepository.existsById(bookingId);
        if (!exists) {
            throw new ResourceNotFundException("CarBooking is not found with " + bookingId);
        }
        carBookingRepository.deleteById(bookingId);
    }
}
