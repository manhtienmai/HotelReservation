package com.example.AuctionCloud.dto;

import com.example.AuctionCloud.model.Role;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String email;
    private Role role;
    private String password;
    private boolean isBanned;
}
