package org.example;
import org.example.car.Brand;
import org.example.car.Car;
import org.example.car.CarRepository;
import org.example.user.AppUser;
import org.example.user.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class CarBookingFinalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarBookingFinalApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(AppUserRepository appUserRepository,
                                        CarRepository carRepository) {
        return args -> {
            AppUser james = new AppUser("James");
            AppUser jamila = new AppUser("Jamila");
            appUserRepository.saveAll(List.of(james, jamila));
            Car tesla = new Car("TES-001", new BigDecimal("29.99"), Brand.TESLA, true);
            Car teslaX = new Car("TESX-002", new BigDecimal("39.99"), Brand.TESLA, true);
            Car toyota = new Car("TOY-003", new BigDecimal("19.99"), Brand.TOYOTA, false);
            Car audi = new Car("AUD-004", new BigDecimal("24.99"), Brand.AUDI, false);
            Car mercedes = new Car("MER-005", new BigDecimal("34.99"), Brand.MERCEDES, false);
            carRepository.saveAll(List.of(tesla, teslaX, toyota,audi ,mercedes ));

        };
    }


}
