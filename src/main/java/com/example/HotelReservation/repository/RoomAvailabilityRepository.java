package com.example.HotelReservation.repository;

import com.example.HotelReservation.model.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {

//    @Query("SELECT ra FROM RoomAvailability ra " +
//            "WHERE ra.hotelRoom.hotel.id = :hotelId " +
//            "AND ra.hotelRoom.roomType.id = :roomTypeId " +
//            "AND ra.date BETWEEN :checkInDate AND :checkOutDate " +
//            "AND ra.isAvailable = true")
//    List<RoomAvailability> findAvailableRooms(
//            @Param("hotelId") Long hotelId,
//            @Param("roomTypeId") Long roomTypeId,
//            @Param("checkInDate") LocalDate checkInDate,
//            @Param("checkOutDate") LocalDate checkOutDate);

    @Query("SELECT ra FROM RoomAvailability ra " +
            "WHERE ra.hotelRoom.roomType.id = :roomTypeId " +
            "AND ra.hotelRoom.roomType.hotel.id = :hotelId " +
            "AND ra.date BETWEEN :checkInDate AND :checkOutDate " +
            "AND ra.isAvailable = true")
    List<RoomAvailability> findAvailableRooms(
            @Param("hotelId") Long hotelId,
            @Param("roomTypeId") Long roomTypeId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate);

    List<RoomAvailability> findByHotelRoomIdAndDateBetween(Long hotelRoomId, LocalDate startDate, LocalDate endDate);
}