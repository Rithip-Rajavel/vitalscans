package com.vitalscan.vitalscans.controller;

import com.vitalscan.vitalscans.dto.AuthRequest;
import com.vitalscan.vitalscans.dto.AuthResponse;
import com.vitalscan.vitalscans.dto.SignupRequest;
import com.vitalscan.vitalscans.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticate a user with username and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.login(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    @Operation(summary = "Register new user", description = "Register a new user with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
    })
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        AuthResponse authResponse = authService.signup(signupRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate JWT token", description = "Validate if the provided JWT token is valid")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token is valid"),
        @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
    })
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        // Token validation is handled by JWT filter, if we reach here, token is valid
        return ResponseEntity.ok().body("Token is valid");
    }
}
