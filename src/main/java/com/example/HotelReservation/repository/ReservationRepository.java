package com.example.HotelReservation.repository;

import com.example.HotelReservation.model.Hotel;
import com.example.HotelReservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuestId(Hotel hotel);
}
