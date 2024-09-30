package com.example.AuctionCloud.service;

import com.example.AuctionCloud.dto.ProfileDto;
import com.example.AuctionCloud.dto.UserDto;
import com.example.AuctionCloud.dto.UserUpdateDto;
import com.example.AuctionCloud.dto.request.LoginRequest;
import com.example.AuctionCloud.dto.request.RefreshTokenRequest;
import com.example.AuctionCloud.dto.request.RegisterRequest;
import com.example.AuctionCloud.dto.response.JwtAuthenticationResponse;
import com.example.AuctionCloud.dto.response.RegisterResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    RegisterResponse registerUser(RegisterRequest registerRequest);
    Optional<UserDto> getUserById(UUID id);
    List<UserDto> getAllUsers();
    Optional<UserDto> updateUser(UUID id, UserUpdateDto userUpdateDto);
    void deleteUser(UUID id);
    JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    Optional<ProfileDto> getProfileByUserId(UUID userId);
    Optional<ProfileDto> updateProfile(UUID userId, ProfileDto profileDto);

}
