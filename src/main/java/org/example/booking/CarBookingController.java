package org.example.booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/bookings")
public class CarBookingController {
    private final CarBookingService carBookingService;
    public CarBookingController(CarBookingService carBookingService) {
        this.carBookingService = carBookingService;
    }
    @PostMapping
    public ResponseEntity<CarBookingResponse> bookCar(@RequestBody CarBookingRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(carBookingService.book(request));
    }
    @DeleteMapping("{bookingId}")
    public void deleteBooking(@PathVariable("bookingId") UUID bookingId){
        carBookingService.deleteCarBooking(bookingId);
    }
    @GetMapping("user/{userId}")
    public ResponseEntity<List<CarBookingResponse>> allCarBookedByUserId(@PathVariable("userId") UUID userId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(carBookingService.allCarBookedByUserId(userId));
    }
    @GetMapping
    public ResponseEntity<List<CarBookingResponse>> allCarBooked(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(carBookingService.getAllCarBookings());
    }
    @GetMapping("bookingId")
    public ResponseEntity<CarBookingResponse> bookingId(@RequestParam("bookingId") UUID bookingId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(carBookingService.getCarBookingByBookingId(bookingId));
    }
}
