package com.example.gadgetstore.controllers;

import com.example.gadgetstore.entities.User;
import com.example.gadgetstore.repositories.UserRepository;
import com.example.gadgetstore.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me/balance")
    public ResponseEntity<?> getMyBalance(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.status(401).build();
        return userRepository.findByUsername(userDetails.getUsername())
                .map(u -> ResponseEntity.ok(Map.of("balance", u.getBalance())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
