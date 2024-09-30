package com.example.AuctionCloud.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
