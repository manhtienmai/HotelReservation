package com.example.HotelReservation.service;

import com.example.HotelReservation.dto.RoomAvailabilityDTO;
import com.example.HotelReservation.exception.ResourceNotFoundException;
import com.example.HotelReservation.mapper.RoomAvailabilityMapper;
import com.example.HotelReservation.model.HotelRoom;
import com.example.HotelReservation.model.RoomAvailability;
import com.example.HotelReservation.repository.HotelRoomRepository;
import com.example.HotelReservation.repository.RoomAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomAvailabilityService {

    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final HotelRoomRepository hotelRoomRepository;
    private final RoomAvailabilityMapper roomAvailabilityMapper;

    public List<RoomAvailabilityDTO> findAvailableRooms(Long hotelId, Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<RoomAvailability> availableRooms = roomAvailabilityRepository.findAvailableRooms(hotelId, roomTypeId, checkInDate, checkOutDate);
        return availableRooms.stream()
                .map(roomAvailabilityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RoomAvailabilityDTO createRoomAvailability(RoomAvailabilityDTO roomAvailabilityDTO) {
        HotelRoom hotelRoom = hotelRoomRepository.findById(roomAvailabilityDTO.getHotelRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel room not found"));

        RoomAvailability roomAvailability = roomAvailabilityMapper.toEntity(roomAvailabilityDTO);
        roomAvailability.setHotelRoom(hotelRoom);
        roomAvailability = roomAvailabilityRepository.save(roomAvailability);

        return roomAvailabilityMapper.toDTO(roomAvailability);
    }

    public RoomAvailabilityDTO getRoomAvailability(Long roomAvailabilityId) {
        RoomAvailability roomAvailability = roomAvailabilityRepository.findById(roomAvailabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Room availability not found"));
        return roomAvailabilityMapper.toDTO(roomAvailability);
    }

    @Transactional
    public RoomAvailabilityDTO updateRoomAvailability(Long roomAvailabilityId, RoomAvailabilityDTO roomAvailabilityDTO) {
        RoomAvailability roomAvailability = roomAvailabilityRepository.findById(roomAvailabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Room availability not found"));

        roomAvailability.setDate(roomAvailabilityDTO.getDate());
        roomAvailability.setAvailable(roomAvailabilityDTO.getIsAvailable());
        roomAvailability.setPrice(roomAvailabilityDTO.getPrice());

        roomAvailability = roomAvailabilityRepository.save(roomAvailability);
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
    public List<RoomAvailabilityDTO> createRoomAvailabilitiesForDateRange(Long hotelRoomId, LocalDate startDate, LocalDate endDate, BigDecimal price) {
        HotelRoom hotelRoom = hotelRoomRepository.findById(hotelRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel room not found"));

        List<RoomAvailability> roomAvailabilities = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            RoomAvailability roomAvailability = new RoomAvailability();
            roomAvailability.setHotelRoom(hotelRoom);
            roomAvailability.setDate(currentDate);
            roomAvailability.setAvailable(true);
            roomAvailability.setPrice(price);
            roomAvailabilities.add(roomAvailability);
            currentDate = currentDate.plusDays(1);
        }

        roomAvailabilities = roomAvailabilityRepository.saveAll(roomAvailabilities);
        return roomAvailabilities.stream()
                .map(roomAvailabilityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<RoomAvailabilityDTO> updateRoomAvailabilitiesForDateRange(Long hotelRoomId, LocalDate startDate, LocalDate endDate, BigDecimal price, Boolean isAvailable) {
        List<RoomAvailability> roomAvailabilities = roomAvailabilityRepository.findByHotelRoomIdAndDateBetween(hotelRoomId, startDate, endDate);

        for (RoomAvailability roomAvailability : roomAvailabilities) {
            roomAvailability.setPrice(price);
            roomAvailability.setAvailable(isAvailable);
        }

        roomAvailabilities = roomAvailabilityRepository.saveAll(roomAvailabilities);
        return roomAvailabilities.stream()
                .map(roomAvailabilityMapper::toDTO)
                .collect(Collectors.toList());
    }
}