package com.example.gadgetstore.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "purchases")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Product product;

    private Integer quantity;

    private Double totalAmount;

    private Instant purchasedAt;
}
