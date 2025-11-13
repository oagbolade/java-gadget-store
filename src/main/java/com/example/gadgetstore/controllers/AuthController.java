package com.example.gadgetstore.controllers;

import com.example.gadgetstore.dtos.*;
import com.example.gadgetstore.entities.User;
import com.example.gadgetstore.repositories.UserRepository;
import com.example.gadgetstore.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody CreateUserRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        User user = User.builder()
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .balance(0.0)
                .roles("ROLE_USER")
                .build();

        userRepository.save(user);

        return ResponseEntity.created(URI.create("/api/users/" + user.getId())).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Validated @RequestBody AuthRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        UserDetails ud = userDetailsService.loadUserByUsername(req.getUsername());
        String jwt = jwtUtil.generateToken(ud);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
