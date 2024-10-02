package com.example.HotelReservation.controller;

import com.example.HotelReservation.dto.ReservationRequestDTO;
import com.example.HotelReservation.dto.ReservationResponseDTO;
import com.example.HotelReservation.model.ReservationStatus;
import com.example.HotelReservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(@Valid @RequestBody ReservationRequestDTO requestDTO) {
        ReservationResponseDTO responseDTO = reservationService.createReservation(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Long reservationId) {
        ReservationResponseDTO responseDTO = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<ReservationResponseDTO> updateReservation(
            @PathVariable Long reservationId,
            @Valid @RequestBody ReservationRequestDTO requestDTO) {
        ReservationResponseDTO responseDTO = reservationService.updateReservation(reservationId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByGuest(@PathVariable Long guestId) {
        List<ReservationResponseDTO> reservations = reservationService.getReservationsByGuest(guestId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByStatus(@PathVariable ReservationStatus status) {
        List<ReservationResponseDTO> reservations = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/{reservationId}/check-in")
    public ResponseEntity<ReservationResponseDTO> checkIn(@PathVariable Long reservationId) {
        ReservationResponseDTO responseDTO = reservationService.checkIn(reservationId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{reservationId}/check-out")
    public ResponseEntity<ReservationResponseDTO> checkOut(@PathVariable Long reservationId) {
        ReservationResponseDTO responseDTO = reservationService.checkOut(reservationId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ReservationResponseDTO>> searchReservations(
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) Long guestId,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {
        Page<ReservationResponseDTO> reservations = reservationService.searchReservations(hotelId, guestId, status, startDate, endDate, pageable);
        return ResponseEntity.ok(reservations);
    }
}