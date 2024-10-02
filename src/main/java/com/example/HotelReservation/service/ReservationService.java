package com.example.HotelReservation.service;

import com.example.HotelReservation.dto.ReservationRequestDTO;
import com.example.HotelReservation.dto.ReservationResponseDTO;
import com.example.HotelReservation.exception.BadRequestException;
import com.example.HotelReservation.exception.ResourceNotFoundException;
import com.example.HotelReservation.mapper.ReservationMapper;
import com.example.HotelReservation.model.*;
import com.example.HotelReservation.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

        List<RoomAvailability> roomAvailabilities = findAvailableRooms(
                requestDTO.getRoomTypeId(),
                requestDTO.getRoomTypeId(),
                requestDTO.getCheckInDate(),
                requestDTO.getCheckOutDate());

        if (roomAvailabilities.isEmpty()) {
            throw new BadRequestException("No available rooms for the specified criteria");
        }

        BigDecimal totalPrice = calculateTotalPrice(roomAvailabilities, requestDTO.getCheckInDate(), requestDTO.getCheckOutDate());

        Reservation reservation = createReservationEntity(guest, requestDTO, totalPrice);
        reservation = reservationRepository.save(reservation);

        List<ReservationRoom> reservationRooms = createReservationRooms(reservation, roomAvailabilities, requestDTO.getCheckInDate(), requestDTO.getCheckOutDate());
        reservationRoomRepository.saveAll(reservationRooms);

        return reservationMapper.toDTO(reservation);
    }

    @Transactional
    public ReservationResponseDTO updateReservation(Long reservationId, ReservationRequestDTO requestDTO) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.PENDING && reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new BadRequestException("Cannot update a reservation that is not in PENDING or CONFIRMED state");
        }

        List<RoomAvailability> newRoomAvailabilities = findAvailableRooms(requestDTO.getRoomTypeId(), requestDTO.getCheckInDate(), requestDTO.getCheckOutDate());

        if (newRoomAvailabilities.isEmpty()) {
            throw new BadRequestException("No available rooms for the specified criteria");
        }

        BigDecimal newTotalPrice = calculateTotalPrice(newRoomAvailabilities, requestDTO.getCheckInDate(), requestDTO.getCheckOutDate());

        updateReservationEntity(reservation, requestDTO, newTotalPrice);

        // Release old room availabilities
        List<ReservationRoom> oldReservationRooms = reservationRoomRepository.findByReservationId(reservationId);
        for (ReservationRoom oldRoom : oldReservationRooms) {
            RoomAvailability oldAvailability = oldRoom.getRoomAvailability();
            oldAvailability.setAvailable(true);
            roomAvailabilityRepository.save(oldAvailability);
        }

        // Remove old reservation rooms
        reservationRoomRepository.deleteByReservationId(reservationId);

        // Create new reservation rooms
        List<ReservationRoom> newReservationRooms = createReservationRooms(reservation, newRoomAvailabilities, requestDTO.getCheckInDate(), requestDTO.getCheckOutDate());
        reservationRoomRepository.saveAll(newReservationRooms);

        reservation = reservationRepository.save(reservation);

        return reservationMapper.toDTO(reservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.CHECKED_IN || reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new BadRequestException("Cannot cancel a reservation that is already checked in or checked out");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // Release room availabilities
        List<ReservationRoom> reservationRooms = reservationRoomRepository.findByReservationId(reservationId);
        for (ReservationRoom reservationRoom : reservationRooms) {
            RoomAvailability roomAvailability = reservationRoom.getRoomAvailability();
            roomAvailability.setAvailable(true);
            roomAvailabilityRepository.save(roomAvailability);
        }
    }

    public ReservationResponseDTO getReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        return reservationMapper.toDTO(reservation);
    }

    public List<ReservationResponseDTO> getReservationsByGuest(Long guestId) {
        List<Reservation> reservations = reservationRepository.findByGuestId(guestId);
        return reservations.stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDTO> getReservationsByStatus(ReservationStatus status) {
        List<Reservation> reservations = reservationRepository.findByStatus(status);
        return reservations.stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponseDTO checkIn(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new BadRequestException("Can only check in CONFIRMED reservations");
        }

        reservation.setStatus(ReservationStatus.CHECKED_IN);
        reservation = reservationRepository.save(reservation);

        return reservationMapper.toDTO(reservation);
    }

    @Transactional
    public ReservationResponseDTO checkOut(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new BadRequestException("Can only check out CHECKED_IN reservations");
        }

        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        reservation = reservationRepository.save(reservation);

        return reservationMapper.toDTO(reservation);
    }

    private List<RoomAvailability> findAvailableRooms(Long hotelId, Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
        return roomAvailabilityRepository.findAvailableRooms(hotelId, roomTypeId, checkInDate, checkOutDate);
    }

    private BigDecimal calculateTotalPrice(List<RoomAvailability> roomAvailabilities, LocalDate checkInDate, LocalDate checkOutDate) {
        // Implementation remains the same
        return null;
    }

    private Reservation createReservationEntity(Guest guest, ReservationRequestDTO requestDTO, BigDecimal totalPrice) {
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setTotalPrice(totalPrice);
        reservation.setAdults(requestDTO.getAdults());
        reservation.setChildren(requestDTO.getChildren());
        reservation.setSpecialRequests(requestDTO.getSpecialRequests());
        return reservation;
    }

    private void updateReservationEntity(Reservation reservation, ReservationRequestDTO requestDTO, BigDecimal newTotalPrice) {
        reservation.setTotalPrice(newTotalPrice);
        reservation.setAdults(requestDTO.getAdults());
        reservation.setChildren(requestDTO.getChildren());
        reservation.setSpecialRequests(requestDTO.getSpecialRequests());
    }

    private List<ReservationRoom> createReservationRooms(Reservation reservation, List<RoomAvailability> roomAvailabilities, LocalDate checkInDate, LocalDate checkOutDate) {
        return roomAvailabilities.stream().map(availability -> {
            ReservationRoom reservationRoom = new ReservationRoom();
            reservationRoom.setReservation(reservation);
            reservationRoom.setRoomAvailability(availability);
            reservationRoom.setCheckInDate(checkInDate);
            reservationRoom.setCheckOutDate(checkOutDate);
            reservationRoom.setPricePerNight(availability.getPrice());

            // Mark room as unavailable
            availability.setAvailable(false);
            roomAvailabilityRepository.save(availability);

            return reservationRoom;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ReservationResponseDTO> searchReservations(Long hotelId, Long guestId, ReservationStatus status,
                                                           LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<Reservation> spec = Specification.where(null);

        if (hotelId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.join("reservationRooms").join("roomAvailability").join("hotelRoom").join("hotel").get("id"), hotelId));
        }
        if (guestId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("guest").get("id"), guestId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) -> cb.and(
                    cb.greaterThanOrEqualTo(root.join("reservationRooms").get("checkInDate"), startDate),
                    cb.lessThanOrEqualTo(root.join("reservationRooms").get("checkOutDate"), endDate)
            ));
        }

        Page<Reservation> reservations = reservationRepository.findAll(spec, pageable);
        return reservations.map(reservationMapper::toDTO);
    }
}