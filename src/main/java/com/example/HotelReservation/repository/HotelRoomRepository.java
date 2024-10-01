package com.example.HotelReservation.repository;

import com.example.HotelReservation.model.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRoomRepository extends JpaRepository<HotelRoom, Long> {
    List<HotelRoom> findByHotelIdAndRoomTypeId(Long hotelId, Long roomTypeId);
}
