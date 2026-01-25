package com.vitalscan.vitalscans.controller;

import com.vitalscan.vitalscans.dto.AuthRequest;
import com.vitalscan.vitalscans.dto.AuthResponse;
import com.vitalscan.vitalscans.dto.SignupRequest;
import com.vitalscan.vitalscans.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.login(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        AuthResponse authResponse = authService.signup(signupRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        // Token validation is handled by JWT filter, if we reach here, token is valid
        return ResponseEntity.ok().body("Token is valid");
    }
}
