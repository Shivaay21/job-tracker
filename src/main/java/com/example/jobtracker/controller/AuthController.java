package com.example.jobtracker.controller;

import com.example.jobtracker.config.JwtUtil;
import com.example.jobtracker.dto.request.AuthRequestDTO;
import com.example.jobtracker.dto.response.AuthResponseDTO;
import com.example.jobtracker.entity.Role;
import com.example.jobtracker.exception.DuplicateResourceException;
import com.example.jobtracker.repository.UserRepository;
import com.example.jobtracker.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "User APIs", description = "Operations related to user registration and authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with encrypted password and default USER role"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/register")
    public AuthResponseDTO register(@Valid @RequestBody AuthRequestDTO requestDTO){
        log.info("Register attempt for email: {}", requestDTO.getEmail());
        if(userRepository.existsByEmail(requestDTO.getEmail())){
            log.warn("Registration failed - email already exists: {}", requestDTO.getEmail());
            throw new DuplicateResourceException("Email already exists");
        }
        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
        log.info("User registered successfully for email: {}",requestDTO.getEmail());
        return new AuthResponseDTO("User registered successfully", null);
    }

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates user credentials and returns JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody AuthRequestDTO requestDTO){
        log.info("Login attempt for email: {}",requestDTO.getEmail());
        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(()->{
                    log.warn("Login failed - invalid credentials for email: {}",requestDTO.getEmail());
                    return new RuntimeException("Invalid email or password");
                });

        if(!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        log.info("Login successful for email: {}",requestDTO.getEmail());
        return new AuthResponseDTO("Login Successful", token);
    }

}
