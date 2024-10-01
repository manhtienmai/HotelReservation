package com.example.HotelReservation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ReservationRequestDTO {
    private Long guestId;
    private Long hotelId;
    private Long roomTypeId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer adults;
    private Integer children;
    private String specialRequests;
    private List<Long> roomAvailabilityIds;
}
