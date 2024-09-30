package com.example.AuctionCloud.service.impl;

import com.example.AuctionCloud.dto.ProfileDto;
import com.example.AuctionCloud.dto.UserDto;
import com.example.AuctionCloud.dto.UserUpdateDto;
import com.example.AuctionCloud.dto.request.LoginRequest;
import com.example.AuctionCloud.dto.request.RefreshTokenRequest;
import com.example.AuctionCloud.dto.request.RegisterRequest;
import com.example.AuctionCloud.dto.response.JwtAuthenticationResponse;
import com.example.AuctionCloud.dto.response.RegisterResponse;
import com.example.AuctionCloud.repository.ProfileRepository;
import com.example.AuctionCloud.repository.UserRepository;
import com.example.AuctionCloud.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private ProfileRepository profileRepository;
    priva
    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        return null;
    }

    @Override
    public Optional<UserDto> getUserById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<UserDto> getAllUsers() {
        return List.of();
    }

    @Override
    public Optional<UserDto> updateUser(UUID id, UserUpdateDto userUpdateDto) {
        return Optional.empty();
    }

    @Override
    public void deleteUser(UUID id) {

    }

    @Override
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return null;
    }

    @Override
    public Optional<ProfileDto> getProfileByUserId(UUID userId) {
        return Optional.empty();
    }

    @Override
    public Optional<ProfileDto> updateProfile(UUID userId, ProfileDto profileDto) {
        return Optional.empty();
    }
}
