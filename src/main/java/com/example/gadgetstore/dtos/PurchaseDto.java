package com.example.gadgetstore.dtos;

import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDto {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Double totalAmount;
    private Instant purchasedAt;
    private String message;
    private Double newBalance;
}
