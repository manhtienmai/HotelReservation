package com.example.AuctionCloud.dto;

import com.example.AuctionCloud.model.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String email;
    private Role role;
    private boolean isBanned;
    private ProfileDto profile;
}
