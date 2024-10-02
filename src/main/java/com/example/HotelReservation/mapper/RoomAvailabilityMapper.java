package com.example.HotelReservation.mapper;

import com.example.HotelReservation.dto.RoomAvailabilityDTO;
import com.example.HotelReservation.model.RoomAvailability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomAvailabilityMapper {

    @Mapping(source = "hotelRoom.id", target = "hotelRoomId")
    RoomAvailabilityDTO toDTO(RoomAvailability roomAvailability);

    @Mapping(source = "hotelRoomId", target = "hotelRoom.id")
    RoomAvailability toEntity(RoomAvailabilityDTO roomAvailabilityDTO);
}