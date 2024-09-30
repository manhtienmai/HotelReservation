package com.example.AuctionCloud.dto.request;

import com.example.AuctionCloud.model.Role;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
    private String fullName;
    private String phoneNumber;
    private MultipartFile profileImage;

}
