package org.example.car;
import java.math.BigDecimal;
import java.util.UUID;
public record CarResponse(
        UUID carId,
        String regNumber,
        BigDecimal rentalPricePerDay,
        Brand brand,
        boolean isElectric
) {
}
