package com.example.gadgetstore.dtos;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
public class CreateUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
