package com.example.HotelReservation.controller;

import com.example.HotelReservation.service.RoomAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room-availabilities")
public class RoomAvailabilityController {

    @Autowired
    private RoomAvailabilityService roomAvailabilityService;

    @GetMapping("/search")
    public ResponseEntity
}
