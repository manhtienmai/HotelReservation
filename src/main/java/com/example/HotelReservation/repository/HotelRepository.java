package com.example.HotelReservation.repository;

import com.example.HotelReservation.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
