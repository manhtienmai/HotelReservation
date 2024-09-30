package com.example.AuctionCloud.dto.response;

import com.example.AuctionCloud.model.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RegisterResponse {
    private UUID id;
    private String email;
    private Role role;
    private String fullName;
    private String phoneNumber;
}
