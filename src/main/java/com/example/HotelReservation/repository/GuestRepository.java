package com.example.HotelReservation.repository;

import com.example.HotelReservation.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Guest findByEmail(String email);
}
