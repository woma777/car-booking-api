package org.example.car;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarService carService;
    public CarController(CarService carService) {
        this.carService = carService;
    }
    @GetMapping("/available")
    public ResponseEntity<List<CarResponse>> getAvailableCars() {
        return ResponseEntity.status(HttpStatus.OK).body(
                carService.getAllCars()
        );
    }
    @GetMapping("/available/electric")
    public ResponseEntity<List<CarResponse>> getAvailableElectricCars() {
        return ResponseEntity.status(HttpStatus.OK).body(
                carService.getAllElectricCars()
        );
    }

}
