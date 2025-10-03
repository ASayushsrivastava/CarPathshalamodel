package com.carPathshala.controllers;

import com.carPathshala.dto.auth.AuthRequest;
import com.carPathshala.dto.auth.AuthResponse;
import com.carPathshala.dto.auth.RegisterRequest;
import com.carPathshala.exceptions.InvalidCredentialsException;
import com.carPathshala.model.User;
import com.carPathshala.repository.UserRepository;
import com.carPathshala.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ✅ REGISTER NEW USER
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body("❌ Username already exists");
        }

        User u = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role("ROLE_USER")  // Default role
                .build();

        userRepository.save(u);
        return ResponseEntity.ok("✅ User registered successfully");
    }

    // ✅ LOGIN AND GENERATE TOKENS
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        // Check if username exists
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        // Validate password
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // Generate access & refresh tokens
        String role = u.getRole() != null ? u.getRole() : "ROLE_USER";
        String accessToken = jwtUtil.generateAccessToken(u.getUsername(), role);
        String refreshToken = jwtUtil.generateRefreshToken(u.getUsername());

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    // ✅ REFRESH ACCESS TOKEN USING REFRESH TOKEN
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody AuthResponse request) {
        String refreshToken = request.getRefreshToken();

        // Validate refresh token
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new InvalidCredentialsException("Invalid or expired refresh token");
        }

        // Generate new access token
        String username = jwtUtil.extractUsername(refreshToken);
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        String accessToken = jwtUtil.generateAccessToken(u.getUsername(), u.getRole());
        String newRefreshToken = jwtUtil.generateRefreshToken(u.getUsername());

        return ResponseEntity.ok(new AuthResponse(accessToken, newRefreshToken));
    }
}