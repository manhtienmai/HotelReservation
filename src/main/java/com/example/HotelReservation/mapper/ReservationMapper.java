package com.example.HotelReservation.mapper;

import com.example.HotelReservation.dto.ReservationResponseDTO;
import com.example.HotelReservation.model.Reservation;
import com.example.HotelReservation.model.ReservationRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "reservation.id", target = "id")
    @Mapping(source = "reservation.guest.id", target = "guestId")
    @Mapping(source = "reservation.status", target = "status")
    @Mapping(source = "reservation.totalPrice", target = "totalPrice")
    @Mapping(source = "reservation.adults", target = "adults")
    @Mapping(source = "reservation.children", target = "children")
    @Mapping(source = "reservation.specialRequests", target = "specialRequests")
    @Mapping(expression = "java(getCheckInDate(reservation))", target = "checkInDate")
    @Mapping(expression = "java(getCheckOutDate(reservation))", target = "checkOutDate")
    ReservationResponseDTO toDTO(Reservation reservation);

    default LocalDate getCheckInDate(Reservation reservation) {
        return reservation.getReservationRooms().stream()
                .findFirst()
                .map(ReservationRoom::getCheckInDate)
                .orElse(null);
    }

    default LocalDate getCheckOutDate(Reservation reservation) {
        return reservation.getReservationRooms().stream()
                .findFirst()
                .map(ReservationRoom::getCheckOutDate)
                .orElse(null);
    }
}