package com.example.gadgetstore.service;

import com.example.gadgetstore.dtos.PurchaseDto;
import com.example.gadgetstore.entities.Product;
import com.example.gadgetstore.entities.Purchase;
import com.example.gadgetstore.entities.User;
import com.example.gadgetstore.mappers.PurchaseMapper;
import com.example.gadgetstore.repositories.ProductRepository;
import com.example.gadgetstore.repositories.PurchaseRepository;
import com.example.gadgetstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PurchaseMapper purchaseMapper;

    public PurchaseDto purchase(PurchaseDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Not enough stock available");
        }

        double totalCost = product.getPrice() * dto.getQuantity();
        if (user.getBalance() < totalCost) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalance(user.getBalance() - totalCost);
        product.setQuantity(product.getQuantity() - dto.getQuantity());

        Purchase purchase = purchaseMapper.toEntity(dto);
        purchase.setPurchasedAt(Instant.from(LocalDateTime.now()));
        purchaseRepository.save(purchase);

        userRepository.save(user);
        productRepository.save(product);

        return purchaseMapper.toDto(purchase);
    }
}
