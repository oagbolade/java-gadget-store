package com.example.gadgetstore.controllers;

import com.example.gadgetstore.dtos.PurchaseDto;
import com.example.gadgetstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final ProductService productService;

    @PostMapping("/{productId}")
    public PurchaseDto purchase(@PathVariable Long productId, Authentication auth) {
        String username = auth.getName();
        return productService.purchaseProduct(productId, username);
    }
}
