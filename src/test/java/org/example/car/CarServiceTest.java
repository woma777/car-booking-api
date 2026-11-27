package org.example.car;
import org.example.booking.CarBookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    @InjectMocks
    private CarService underTest;
    @Mock
    private CarBookingRepository carBookingRepository;
    @Mock
    private CarRepository carRepository;
    @Test
    void shouldGetAllCars() {
        // Given
        Car tesla = new Car("TES-001", new BigDecimal("29.99"), Brand.TESLA,
                true);
        Car toyota = new Car("TOY-003", new BigDecimal("19.99"), Brand.TOYOTA,
                false);
        Car audi = new Car("AUD-004", new BigDecimal("24.99"), Brand.AUDI,
                false);
        tesla.setCarId(UUID.randomUUID());
        toyota.setCarId(UUID.randomUUID());
        audi.setCarId(UUID.randomUUID());
        List<Car> cars = List.of(tesla, toyota, audi);
        when(carRepository.findAll()).thenReturn(cars);
        List<CarResponse> expected = List.of(
                new CarResponse(tesla.getCarId(), "TES-001", new BigDecimal("29.99"),
                        Brand.TESLA,
                        true
                ),
                new CarResponse(toyota.getCarId(), "TOY-003", new BigDecimal("19.99"),
                        Brand.TOYOTA,
                        false
                ),
                new CarResponse(audi.getCarId(), "AUD-004", new BigDecimal("24.99"),
                        Brand.AUDI,
                        false
                )
        );
        // When
        List<CarResponse> actual = underTest.getAllCars();
        // Then
        verify(carRepository).findAll();
        assertThat(actual).hasSize(3).containsExactlyElementsOf(expected);
    }
    @Test
    void shouldGetAllElectricCars() {
        // Given
        Car tesla = new Car("TES-001", new BigDecimal("29.99"), Brand.TESLA,
                true);
        tesla.setCarId(UUID.randomUUID());
        when(carRepository.findByIsElectricTrue())
                .thenReturn(List.of(tesla));
        List<CarResponse> expected = List.of(
                new CarResponse(tesla.getCarId(), "TES-001", new BigDecimal("29.99"),
                        Brand.TESLA,
                        true
                )
        );
        // When
        List<CarResponse> actual = underTest.getAllElectricCars();
        // Then
        verify(carRepository).findByIsElectricTrue();
        assertThat(actual).containsExactlyElementsOf(expected);
    }
    @Test
    void getCarById() {
        //given
        Car tesla = new Car("TES-001", new BigDecimal("29.99"), Brand.TESLA,
                true);
        tesla.setCarId(UUID.randomUUID());
        when(carRepository.findById(tesla.getCarId())).thenReturn(Optional.of(tesla));
        //when
        CarResponse actual = underTest.getCarById(tesla.getCarId());
        //then
        verify(carRepository).findById(tesla.getCarId());
        assertThat(actual).isNotNull();
        assertThat(actual.carId()).isEqualTo(tesla.getCarId());
    }
}