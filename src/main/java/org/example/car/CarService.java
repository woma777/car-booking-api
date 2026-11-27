package org.example.car;
import org.example.booking.BookingStatus;
import org.example.booking.CarBookingRepository;
import org.example.exception.ResourceNotFundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class CarService {
    private final CarBookingRepository carBookingRepository;
    private final CarRepository carRepository;
    public CarService(CarBookingRepository carBookingRepository, CarRepository carRepository) {
        this.carBookingRepository = carBookingRepository;
        this.carRepository = carRepository;
    }
    public List<CarResponse> getAllCars(){
        Set<UUID> bookedCarIds =carBookingRepository
                .findByStatus(BookingStatus.ACTIVE)
                .stream()
                .map(booking -> booking.getCar().getCarId())
                .collect(Collectors.toSet());
        return carRepository.findAll()
                .stream()
                .filter(car -> !bookedCarIds.contains(car.getCarId()))
                .map(car -> new CarResponse(
                        car.getCarId(),
                        car.getRegNumber(),
                        car.getRentalPricePerDay(),
                        car.getBrand(),
                        car.isElectric()
                ))
                .collect(Collectors.toList());
    }
    public List<CarResponse> getAllElectricCars(){
        Set<UUID> bookedCarIds = getBookedCarIds();
        return carRepository.findByIsElectricTrue()
                .stream()
                .filter(car -> !bookedCarIds.contains(car.getCarId()))
                .map(car -> new CarResponse(
                        car.getCarId(),
                        car.getRegNumber(),
                        car.getRentalPricePerDay(),
                        car.getBrand(),
                        car.isElectric()
                ))
                .collect(Collectors.toList());
    }
    private Set<UUID> getBookedCarIds() {
        return carBookingRepository.findByStatus(BookingStatus.ACTIVE)
                .stream()
                .map(booking -> booking.getCar().getCarId())
                .collect(Collectors.toSet());
    }
    public CarResponse getCarById(UUID carId){
       Car car = carRepository.findById(carId)
               .orElseThrow(()-> new ResourceNotFundException("Car not found in this id:" + carId));
       return new CarResponse(
               car.getCarId(),
               car.getRegNumber(),
               car.getRentalPricePerDay(),
               car.getBrand(),
               car.isElectric()
       );
    }
    

}
