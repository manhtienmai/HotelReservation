package com.example.HotelReservation.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RoomAvailabilityDTO {
    private Long id;
    private Long hotelRoomId;
    private LocalDate date;
    private Boolean isAvailable;
    private BigDecimal price;
}
