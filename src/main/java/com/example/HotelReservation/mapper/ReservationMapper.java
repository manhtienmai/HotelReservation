package com.example.HotelReservation.mapper;

@Mapper(componentModel = "spring", config = MapStructConfig.class)
public interface ReservationMapper {

    @Mapping(source = "reservation.id", target = "id")
    @Mapping(source = "reservation.guest.id", target = "guestId")
    @Mapping(source = "reservation.status", target = "status")
    @Mapping(source = "reservation.totalPrice", target = "totalPrice")
    @Mapping(source = "reservation.adults", target = "adults")
    @Mapping(source = "reservation.children", target = "children")
    @Mapping(source = "reservation.specialRequests", target = "specialRequests")
    @Mapping(source = "checkInDate", target = "checkInDate")
    @Mapping(source = "checkOutDate", target = "checkOutDate")
    ReservationResponseDTO toDTO(Reservation reservation, LocalDate checkInDate, LocalDate checkOutDate);
}