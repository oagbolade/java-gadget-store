package com.example.gadgetstore.repositories;

import com.example.gadgetstore.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    // find purchases by user etc
}
