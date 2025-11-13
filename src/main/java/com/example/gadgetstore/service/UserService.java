package com.example.gadgetstore.service;

import com.example.gadgetstore.dtos.UserDto;
import com.example.gadgetstore.entities.User;
import com.example.gadgetstore.mappers.UserMapper;
import com.example.gadgetstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDto createUser(UserDto dto, String rawPassword) {
        User user = userMapper.toEntity(dto);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setBalance(dto.getBalance() != null ? dto.getBalance() : 0.0);

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public Double getBalance(Long userId) {
        return userRepository.findById(userId)
                .map(User::getBalance)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
