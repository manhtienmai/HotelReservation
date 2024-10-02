package com.example.HotelReservation.repository;

import com.example.HotelReservation.model.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {

    List<ReservationRoom> findByReservationId(Long reservationId);

    void deleteByReservationId(Long reservationId);
}