package org.example.booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;
public interface CarBookingRepository extends JpaRepository<CarBooking, UUID> {

    List<CarBooking> findByUser_UserId(UUID userId);
    @Query("""
        SELECT b FROM CarBooking b
        WHERE b.status = :status
        """)
    List<CarBooking> findByStatus(
            @Param("status") BookingStatus status
    );

}
