package com.example.gadgetstore.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private Double balance = 0.0;

    private String roles; // simple comma separated roles for now (e.g. ROLE_USER)
}
