package com.example.gadgetstore.service;

import com.example.gadgetstore.dtos.ProductDto;
import com.example.gadgetstore.dtos.PurchaseDto;
import com.example.gadgetstore.entities.Product;
import com.example.gadgetstore.entities.User;
import com.example.gadgetstore.mappers.ProductMapper;
import com.example.gadgetstore.repositories.ProductRepository;
import com.example.gadgetstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;

    @CacheEvict(value = "products", allEntries = true)
    public ProductDto addProduct(ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Cacheable(value = "products")
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @CacheEvict(value = "products", allEntries = true)
    public PurchaseDto purchaseProduct(Long productId, String username) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (product.getQuantity() <= 0) {
            throw new RuntimeException("Product out of stock");
        }

        if (user.getBalance() < product.getPrice()) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalance(user.getBalance() - product.getPrice());
        product.setQuantity(product.getQuantity() - 1);

        userRepository.save(user);
        productRepository.save(product);

        PurchaseDto response = new PurchaseDto();
        response.setMessage("Purchase successful!");
        response.setNewBalance(user.getBalance());

        return response;
    }

}
