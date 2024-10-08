package com.example.HotelReservation.repository;

import com.example.HotelReservation.model.Reservation;
import com.example.HotelReservation.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

    List<Reservation> findByGuestId(Long guestId);

    List<Reservation> findByStatus(ReservationStatus status);

    @Query("SELECT DISTINCT r FROM Reservation r JOIN r.reservationRooms rr WHERE rr.roomAvailability.hotelRoom.hotel.id = :hotelId AND rr.checkInDate >= :startDate AND rr.checkOutDate <= :endDate")
    List<Reservation> findByHotelAndDateRange(
            @Param("hotelId") Long hotelId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}