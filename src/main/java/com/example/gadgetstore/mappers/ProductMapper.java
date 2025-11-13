package com.example.gadgetstore.mappers;

import org.mapstruct.Mapper;
import com.example.gadgetstore.entities.Product;
import com.example.gadgetstore.dtos.ProductDto;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toEntity(ProductDto dto);
}
