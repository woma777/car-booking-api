package org.example.booking;
import org.example.car.Brand;
import org.example.car.Car;
import org.example.car.CarRepository;
import org.example.exception.ResourceNotFundException;
import org.example.user.AppUser;
import org.example.user.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarBookingServiceTest {
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarBookingRepository carBookingRepository;
    @InjectMocks
    private CarBookingService underTest;

    @BeforeEach
    void setUp() {
        underTest= new CarBookingService(carBookingRepository, appUserRepository, carRepository);
    }
    @Test
    void shouldBookCarSuccessfully() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        AppUser user = new AppUser("James");
        user.setUserId(userId);
        Car car = new Car("TES-001", new BigDecimal("29.99"), Brand.TESLA, true
        );
        car.setCarId(carId);
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(5);
        BigDecimal expectedPrice = new BigDecimal("149.95");
        CarBookingRequest request = new CarBookingRequest(userId, carId, startDate,
                endDate, BookingStatus.ACTIVE);
        when(appUserRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(carRepository.findById(carId))
                .thenReturn(Optional.of(car));
        UUID bookingId = UUID.randomUUID();
        when(carBookingRepository.save(any(CarBooking.class)))
                .thenAnswer(invocation -> {
                    CarBooking booking = invocation.getArgument(0);
                    booking.setBookingId(bookingId);
                    return booking;
                });
        // When
        CarBookingResponse actual = underTest.book(request);
        // Then
        ArgumentCaptor<CarBooking> argumentCaptor = ArgumentCaptor.forClass(CarBooking.class);
        verify(carBookingRepository).save(argumentCaptor.capture());
        CarBooking capturedBooking = argumentCaptor.getValue();
        assertThat(capturedBooking.getUser()).isEqualTo(user);
        assertThat(capturedBooking.getCar()).isEqualTo(car);
        assertThat(capturedBooking.getStartDate()).isEqualTo(startDate);
        assertThat(capturedBooking.getEndDate()).isEqualTo(endDate);
        assertThat(capturedBooking.getPrice()).isEqualByComparingTo(expectedPrice);
        assertThat(capturedBooking.getStatus()).isEqualTo(BookingStatus.ACTIVE);
        assertThat(capturedBooking.getBookedAt()).isNotNull();
        assertThat(actual).isNotNull();
        assertThat(actual.price()).isEqualByComparingTo(expectedPrice);
        assertThat(actual.userName()).isEqualTo("James");
        assertThat(actual.carRegNumber()).isEqualTo("TES-001");
        assertThat(actual.status()).isEqualTo(BookingStatus.ACTIVE);
        assertThat(actual.startDate()).isEqualTo(startDate);
        assertThat(actual.endDate()).isEqualTo(endDate);
    }

    @Test
    void shouldGetAllCarBookings() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        AppUser user = new AppUser("James");
        user.setUserId(userId);
        Car car = new Car("TES-001", new BigDecimal("29.99"), Brand.TESLA,
                true);
        car.setCarId(carId);
        LocalDate startDate = LocalDate.of(2026, 12, 1);
        LocalDate endDate = startDate.plusDays(5);
        CarBooking booking = new CarBooking(
                user,
                car,
                startDate,
                endDate,
                new BigDecimal("149.95"),
                BookingStatus.ACTIVE,
                LocalDateTime.now()
        );
        booking.setBookingId(bookingId);
        when(carBookingRepository.findAll()).thenReturn(List.of(booking));
        // When
        List<CarBookingResponse> actual = underTest.getAllCarBookings();
        // Then
        verify(carBookingRepository).findAll();
        assertThat(actual).hasSize(1);
        CarBookingResponse response = actual.getFirst();
        assertThat(response.id()).isEqualTo(bookingId);
        assertThat(response.userName()).isEqualTo("James");
        assertThat(response.carRegNumber()).isEqualTo("TES-001");
        assertThat(response.carBrand()).isEqualTo(Brand.TESLA);
        assertThat(response.startDate()).isEqualTo(startDate);
        assertThat(response.endDate()).isEqualTo(endDate);
        assertThat(response.price()).isEqualByComparingTo("149.95");
        assertThat(response.status()).isEqualTo(BookingStatus.ACTIVE);
    }

    @Test
    void shouldGetCarBookingByBookingId() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        AppUser user = new AppUser("James");
        user.setUserId(userId);
        Car car = new Car("TES-001", new BigDecimal("29.99"), Brand.TESLA,
                true);
        car.setCarId(carId);
        LocalDate startDate = LocalDate.of(2026, 12, 1);
        LocalDate endDate = startDate.plusDays(5);
        LocalDateTime bookedAt = LocalDateTime.now();
        CarBooking booking = new CarBooking(
                user,
                car,
                startDate,
                endDate,
                new BigDecimal("149.95"),
                BookingStatus.ACTIVE,
                bookedAt
        );
        booking.setBookingId(bookingId);
        when(carBookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        // When
        CarBookingResponse actual = underTest.getCarBookingByBookingId(bookingId);
        // Then
        verify(carBookingRepository).findById(bookingId);
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(bookingId);
        assertThat(actual.userName()).isEqualTo("James");
        assertThat(actual.carRegNumber()).isEqualTo("TES-001");
        assertThat(actual.carBrand()).isEqualTo(Brand.TESLA);
        assertThat(actual.startDate()).isEqualTo(startDate);
        assertThat(actual.endDate()).isEqualTo(endDate);
        assertThat(actual.price()).isEqualByComparingTo("149.95");
        assertThat(actual.status()).isEqualTo(BookingStatus.ACTIVE);
        assertThat(actual.bookedAt()).isEqualTo(bookedAt);
    }
    @Test
    void shouldDeleteCarBooking() {
        // Given
        UUID bookingId = UUID.randomUUID();
        when(carBookingRepository.existsById(bookingId)).thenReturn(true);
        // When
        underTest.deleteCarBooking(bookingId);
        // Then
        verify(carBookingRepository).existsById(bookingId);
        verify(carBookingRepository).deleteById(bookingId);
    }
    @Test
    void shouldThrowWhenDeletingNonExistingBooking() {
        // Given
        UUID bookingId = UUID.randomUUID();
        when(carBookingRepository.existsById(bookingId)).thenReturn(false);
        // When & Then
        assertThatThrownBy(() ->
                underTest.deleteCarBooking(bookingId))
                .isInstanceOf(ResourceNotFundException.class)
                .hasMessage("CarBooking is not found with " + bookingId);
        verify(carBookingRepository).existsById(bookingId);
        verify(carBookingRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void shouldThrowWhenGettingNonExistingBook() {
        // Given
        UUID bookingId = UUID.randomUUID();
        when(carBookingRepository.existsById(bookingId)).thenReturn(false);
        // When & Then
        assertThatThrownBy(() ->
                underTest.deleteCarBooking(bookingId))
                .isInstanceOf(ResourceNotFundException.class)
                .hasMessage("CarBooking is not found with " + bookingId);
        verify(carBookingRepository).existsById(bookingId);
    }
}