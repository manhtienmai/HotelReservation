package com.example.HotelReservation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Min(1)
    private Integer adults;

    @Min(0)
    private Integer children;
    private String specialRequests;
    private List<Long> roomAvailabilityIds;
}
