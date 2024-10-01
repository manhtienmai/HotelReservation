package com.example.HotelReservation.service;

import com.example.HotelReservation.dto.ReservationRequestDTO;
import com.example.HotelReservation.dto.ReservationResponseDTO;
import com.example.HotelReservation.exception.BadRequestException;
import com.example.HotelReservation.exception.ResourceNotFoundException;
import com.example.HotelReservation.model.Guest;
import com.example.HotelReservation.model.Reservation;
import com.example.HotelReservation.model.ReservationRoom;
import com.example.HotelReservation.model.RoomAvailability;
import com.example.HotelReservation.repository.GuestRepository;
import com.example.HotelReservation.repository.ReservationRepository;
import com.example.HotelReservation.repository.ReservationRoomRepository;
import com.example.HotelReservation.repository.RoomAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final GuestRepository guestRepository;
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationRoomRepository reservationRoomRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO requestDTO) {
        Guest guest = guestRepository.findById(requestDTO.getGuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        List<RoomAvailability> roomAvailabilities = roomAvailabilityRepository.findAllById(requestDTO.getRoomAvailabilityIds());
        validateRoomAvailabilities(roomAvailabilities, requestDTO.getCheckInDate(), requestDTO.getCheckOutDate());

        BigDecimal totalPrice = calculateTotalPrice(roomAvailabilities, requestDTO.getCheckInDate(), requestDTO.getCheckOutDate());

        Reservation reservation = createReservationEntity(guest, requestDTO, totalPrice);
        reservation = reservationRepository.save(reservation);

        List<ReservationRoom> reservationRooms = createReservationRooms(reservation, roomAvailabilities, requestDTO.getCheckInDate(), requestDTO.getCheckOutDate());
        reservationRoomRepository.saveAll(reservationRooms);

        return reservationMapper.toDto(reservation, requestDTO.getCheckInDate(), requestDTO.getCheckOutDate());
    }

    @Transactional
    public ReservationResponseDTO updateReservation(ReservationRequestDTO requestDTO) {
        // do later
        return null;
    }

    public ReservationResponseDTO getReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        List<ReservationRoom> reservationRooms = reservationRoomRepository.findByReservationId(reservationId);
        LocalDate checkInDate = reservationRooms.get(0).getCheckInDate();
        LocalDate checkOutDate = reservationRooms.get(0).getCheckOutDate();

        return reservationMapper.toDTO(reservation, checkInDate, checkOutDate);
    }

    private void validateRoomAvailabilities(List<RoomAvailability> roomAvailabilities, LocalDate checkInDate, LocalDate checkOutDate) {
        if (roomAvailabilities.isEmpty()) {
            throw new BadRequestException("Room availabilities are empty");
        }
        validateDates(checkInDate, checkOutDate);
    }

    private void validateDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new BadRequestException("Check-in must be before check-out");
        }
        if (checkInDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("Check-in date cant be in the past");
        }
    }

    private BigDecimal calculateTotalPrice(List<RoomAvailability> roomAvailabilities, LocalDate checkInDate, LocalDate checkOutDate) {
        long night = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return roomAvailabilities.stream()
                .map(RoomAvailability::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(night));
    }

}
