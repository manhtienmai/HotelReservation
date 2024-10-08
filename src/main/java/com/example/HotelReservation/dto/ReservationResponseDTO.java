package com.example.HotelReservation.dto;

import com.example.HotelReservation.model.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReservationResponseDTO {
    private Long id;
    private Long guestId;
    private ReservationStatus status;
    private BigDecimal totalPrice;
    private Integer adults;
    private Integer children;
    private String specialRequests;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
