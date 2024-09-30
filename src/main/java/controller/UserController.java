package controller;

import com.example.AuctionCloud.dto.request.LoginRequest;
import com.example.AuctionCloud.dto.request.RegisterRequest;
import com.example.AuctionCloud.dto.response.JwtAuthenticationResponse;
import com.example.AuctionCloud.dto.response.RegisterResponse;
import com.example.AuctionCloud.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestPart("registerRequest") RegisterRequest registerRequest,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage)
    {
        registerRequest.setProfileImage(profileImage);
        RegisterResponse response = userService.createUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse response = userService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }


}
