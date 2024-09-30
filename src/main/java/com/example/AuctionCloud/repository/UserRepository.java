package com.example.AuctionCloud.repository;

import com.example.AuctionCloud.dto.UserDto;
import com.example.AuctionCloud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<UserDto> findByEmail(String email);
    boolean existsByEmail(String email);
}
