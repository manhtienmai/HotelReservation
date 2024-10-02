package com.example.HotelReservation.controller;

import com.example.HotelReservation.dto.RoomAvailabilityDTO;
import com.example.HotelReservation.service.RoomAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/room-availabilities")
@RequiredArgsConstructor
public class RoomAvailabilityController {

    private final RoomAvailabilityService roomAvailabilityService;

    @GetMapping("/search")
    public ResponseEntity<List<RoomAvailabilityDTO>> searchAvailableRooms(
            @RequestParam Long hotelId,
            @RequestParam Long roomTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {
        List<RoomAvailabilityDTO> availableRooms = roomAvailabilityService.findAvailableRooms(hotelId, roomTypeId, checkInDate, checkOutDate);
        return ResponseEntity.ok(availableRooms);
    }

    @PostMapping
    public ResponseEntity<RoomAvailabilityDTO> createRoomAvailability(@Valid @RequestBody RoomAvailabilityDTO roomAvailabilityDTO) {
        RoomAvailabilityDTO createdRoomAvailability = roomAvailabilityService.createRoomAvailability(roomAvailabilityDTO);
        return new ResponseEntity<>(createdRoomAvailability, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<RoomAvailabilityDTO>> createRoomAvailabilitiesForDateRange(
            @RequestParam Long hotelRoomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam BigDecimal price) {
        List<RoomAvailabilityDTO> createdAvailabilities = roomAvailabilityService.createRoomAvailabilitiesForDateRange(hotelRoomId, startDate, endDate, price);
        return new ResponseEntity<>(createdAvailabilities, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomAvailabilityDTO> getRoomAvailability(@PathVariable Long id) {
        RoomAvailabilityDTO roomAvailabilityDTO = roomAvailabilityService.getRoomAvailability(id);
        return ResponseEntity.ok(roomAvailabilityDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomAvailabilityDTO> updateRoomAvailability(
            @PathVariable Long id,
            @Valid @RequestBody RoomAvailabilityDTO roomAvailabilityDTO) {
        RoomAvailabilityDTO updatedRoomAvailability = roomAvailabilityService.updateRoomAvailability(id, roomAvailabilityDTO);
        return ResponseEntity.ok(updatedRoomAvailability);
    }

    @PutMapping("/batch")
    public ResponseEntity<List<RoomAvailabilityDTO>> updateRoomAvailabilitiesForDateRange(
            @RequestParam Long hotelRoomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam BigDecimal price,
            @RequestParam Boolean isAvailable) {
        List<RoomAvailabilityDTO> updatedAvailabilities = roomAvailabilityService.updateRoomAvailabilitiesForDateRange(hotelRoomId, startDate, endDate, price, isAvailable);
        return ResponseEntity.ok(updatedAvailabilities);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomAvailability(@PathVariable Long id) {
        roomAvailabilityService.deleteRoomAvailability(id);
        return ResponseEntity.noContent().build();
    }
}