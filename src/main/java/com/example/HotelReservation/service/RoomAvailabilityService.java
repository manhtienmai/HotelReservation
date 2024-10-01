package com.example.HotelReservation.service;

import com.example.HotelReservation.dto.RoomAvailabilityDTO;
import com.example.HotelReservation.exception.ResourceNotFoundException;
import com.example.HotelReservation.mapper.RoomAvailabilityMapper;
import com.example.HotelReservation.model.RoomAvailability;
import com.example.HotelReservation.repository.RoomAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomAvailabilityService {

    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final RoomAvailabilityMapper roomAvailabilityMapper;

    public List<RoomAvailabilityDTO> findAvailableRooms(Long hotelId, Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<RoomAvailability> availableRooms = roomAvailabilityRepository.findAvailableRooms(hotelId, roomTypeId, checkInDate, checkOutDate);
        return availableRooms.stream()
                .map(roomAvailabilityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateAvailability(Long roomAvailabilityId, boolean isAvailable) {
        RoomAvailability roomAvailability = roomAvailabilityRepository.findById(roomAvailabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Room availability not found"));
        roomAvailability.setIsAvailable(isAvailable);
        roomAvailabilityRepository.save(roomAvailability);
    }

    @Transactional
    public void createRoomAvailability(RoomAvailabilityDTO roomAvailabilityDTO) {
        RoomAvailability roomAvailability = roomAvailabilityMapper.toEntity(roomAvailabilityDTO);
        roomAvailabilityRepository.save(roomAvailability);
    }

    public RoomAvailabilityDTO getRoomAvailability(Long roomAvailabilityId) {
        RoomAvailability roomAvailability = roomAvailabilityRepository.findById(roomAvailabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Room availability not found"));
        return roomAvailabilityMapper.toDTO(roomAvailability);
    }

    @Transactional
    public void deleteRoomAvailability(Long roomAvailabilityId) {
        if (!roomAvailabilityRepository.existsById(roomAvailabilityId)) {
            throw new ResourceNotFoundException("Room availability not found");
        }
        roomAvailabilityRepository.deleteById(roomAvailabilityId);
    }

    @Transactional
    public void updateRoomAvailabilityPrice(Long roomAvailabilityId, Double newPrice) {
        RoomAvailability roomAvailability = roomAvailabilityRepository.findById(roomAvailabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Room availability not found"));
        roomAvailability.setPrice(newPrice);
        roomAvailabilityRepository.save(roomAvailability);
    }
}