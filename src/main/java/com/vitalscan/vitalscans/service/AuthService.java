package com.vitalscan.vitalscans.service;

import com.vitalscan.vitalscans.dto.AuthRequest;
import com.vitalscan.vitalscans.dto.AuthResponse;
import com.vitalscan.vitalscans.dto.SignupRequest;
import com.vitalscan.vitalscans.entity.User;
import com.vitalscan.vitalscans.repository.UserRepository;
import com.vitalscan.vitalscans.security.CustomUserDetailsService;
import com.vitalscan.vitalscans.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public AuthResponse login(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getIdentifier(), authRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = (User) userDetails;

            String token = jwtUtil.generateToken(userDetails);

            return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username/password");
        }
    }

    public AuthResponse signup(SignupRequest signupRequest) {
        // Check if user already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByRollNumber(signupRequest.getRollNumber())) {
            throw new RuntimeException("Roll number already exists");
        }
        if (userRepository.existsByMobileNumber(signupRequest.getMobileNumber())) {
            throw new RuntimeException("Mobile number already exists");
        }

        // Create new user
        User user = User.builder()
            .username(signupRequest.getUsername())
            .password(passwordEncoder.encode(signupRequest.getPassword()))
            .email(signupRequest.getEmail())
            .rollNumber(signupRequest.getRollNumber())
            .mobileNumber(signupRequest.getMobileNumber())
            .role(signupRequest.getRole())
            .height(signupRequest.getHeight())
            .weight(signupRequest.getWeight())
            .firstName(signupRequest.getFirstName())
            .lastName(signupRequest.getLastName())
            .isActive(true)
            .build();

        User savedUser = userRepository.save(user);

        // Generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
            .token(token)
            .id(savedUser.getId())
            .username(savedUser.getUsername())
            .email(savedUser.getEmail())
            .role(savedUser.getRole().name())
            .firstName(savedUser.getFirstName())
            .lastName(savedUser.getLastName())
            .build();
    }
}
