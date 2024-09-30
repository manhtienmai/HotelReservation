package com.example.AuctionCloud.repository;

import com.example.AuctionCloud.dto.ProfileDto;
import com.example.AuctionCloud.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<ProfileDto> findByUserId(UUID userId);
}
