package com.example.AuctionCloud.dto;

import com.example.AuctionCloud.model.AddressType;
import lombok.Data;

import java.util.UUID;

@Data
public class AddressDto {
    private UUID id;
    private UUID userId;
    private AddressType addressType;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}