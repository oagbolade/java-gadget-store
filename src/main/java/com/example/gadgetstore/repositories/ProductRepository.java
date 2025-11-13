package com.example.gadgetstore.repositories;

import com.example.gadgetstore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // later we can add search methods
}
